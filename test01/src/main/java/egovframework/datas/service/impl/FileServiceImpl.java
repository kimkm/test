package egovframework.datas.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.nio.file.Paths;

import javax.annotation.Resource;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.springframework.stereotype.Service;

import egovframework.datas.service.FileService;
import egovframework.datas.service.FileVO;

@Service("fileService")
public class FileServiceImpl implements FileService {
	
	@Resource(name="fileDAO")
	FileDAO fileDAO;

	@Override
	public String insertFiles(FileVO vo) throws Exception {
		return fileDAO.insertFiles(vo);
	}

	@Override
	public List<?> selectFiles(FileVO vo) throws Exception {
		return fileDAO.selectFiles(vo);
	}
	
	@Override
	public int totalFiles() throws Exception {
		return fileDAO.totalFiles();
	}

	@Override
	public int deleteFiles(int id) throws Exception {
		return fileDAO.deleteFiles(id);
	}

	@Override
	public boolean filterFiles(FileVO vo, String path) throws Exception {
		String inputFilePath = path + vo.getFilename();      // 입력 WAV 파일 경로
		String outputFilePath = path + "F_" + vo.getFilename();   // 출력 WAV 파일 경로
		vo.setFilename("F_"+vo.getFilename());
		
		try {
			// 입력 WAV 파일 로드
			ArrayList<Float> audioSamples = loadAudioSamples(inputFilePath);

			// 필터링 수행하여 노이즈 제거
			Filtering filtering = new Filtering();
			double sampleRate = getSampleRate(inputFilePath);
			double cutoffFreq = 2000;   //  20000Hz로 설정
			double filterOrder = 10;    //  10차 필터로 설정
			int filterType = 0;         // 로우패스 필터로 설정
			double ripplePercent = 0;   // 리플 없음
			double[] filteredSamples = filtering.filterSignal(audioSamples, sampleRate, cutoffFreq, filterOrder, filterType, ripplePercent);

			// 노이즈 제거된 오디오 저장
			saveAudioSamples(filteredSamples, outputFilePath, sampleRate);

			System.out.println("노이즈 제거 완료");
			fileDAO.filterFiles(vo);
			return true;
		} catch (IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean noisefilterFiles(FileVO vo, String path) throws Exception {
		System.out.println("pythonbuilder ");
        String pythonScriptPath = path + "filter2.py";
        String[] cmd = new String[4];
        cmd[0] = "python"; // "python3"; // 
        cmd[1] = pythonScriptPath;
        cmd[2] = path + vo.getFilename(); // the first argument
        cmd[3] = path + "F_" + vo.getFilename(); // the second argument

        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec(cmd);

        BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line = "";
        while((line = bfr.readLine()) != null) {
            // display each output line from python script
            System.out.println(line);
            vo.setFilename("F_"+vo.getFilename());
            fileDAO.filterFiles(vo);
            return true;
        }
		return false;
	}
	
	//윈도우 파이썬 필터링 실행
	@Override
	public boolean noisefilterFiles2(FileVO vo, String path) throws Exception {
		System.out.println("%%%%%%%%%%%%%%%%%윈도우ProcessBuilder%%%%%%%%%%%%%%%%%%%%%%%%%%%% ");
        String[] cmd = new String[4];
        cmd[0] =  "python";
        cmd[1] = path + "filter2.py";
        cmd[2] = path + vo.getFilename(); // the first argument
        cmd[3] = path + "F_" + vo.getFilename(); // the second argument
        
		ProcessBuilder processBuilder = new ProcessBuilder(cmd[0],cmd[1],cmd[2],cmd[3]);//
		Process process = processBuilder.start();
		
		//실행결과 가져오기
		InputStream inputStream = process.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		// 2. 프로세스 종료 대기
		int exitCode = process.waitFor();
		System.out.println("종료 코드: " + exitCode);
		// 4. 종료 코드 확인
		exitCode = process.exitValue();
		System.out.println("외부 프로그램 종료 코드: " + exitCode);
		String line;
		while ((line = reader.readLine()) != null) {
			// 실행 결과 처리
		    System.out.println(line);
		    vo.setFilename("F_"+vo.getFilename());
            fileDAO.filterFiles(vo);
            return true;
		}
		
		// 1. 오류 메시지 출력
		InputStream errorStream = process.getErrorStream();
		BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));

		String errorLine;
		while ((errorLine = errorReader.readLine()) != null) {
		    // 오류 처리
			System.out.println("오류 코드: " + errorLine);
		}

		return false;
	}
	
	//리눅스 파이썬 필터링
	@Override
	public boolean noisefilterFiles3(FileVO vo, String path) throws Exception {
		 System.out.println("@@@@@@@@@@@@Linux ProcessBuilder@@@@@@@@@@@@@@@@@ ");
		    String[] cmd = new String[4];
		    cmd[0] = "python3";//"bash";// 
		    cmd[1] = path + "filter2.py"; // "filter.sh";
		    cmd[2] = path + vo.getFilename().replace("\\", "/"); // 파일 경로에서 역슬래시를 슬래시로 변경
		    cmd[3] = path + "F_" + vo.getFilename().replace("\\", "/");

		    ProcessBuilder processBuilder = new ProcessBuilder(cmd);
		    Map<String, String> environment = processBuilder.environment();
		    environment.put("PATH", "/usr/bin:" + environment.get("PATH"));
		    environment.put("NUMBA_CACHE_DIR", "/tmp/numba_cache"); // 리눅스 설정 : mkdir /tmp/numba_cache & chmod 777 /tmp/numba_cache
		    processBuilder.redirectErrorStream(true);
		    processBuilder.inheritIO();

		    try {
		        Process process = processBuilder.start();
		        // 일정 시간 내에 프로세스가 완료되지 않으면 오류로 처리
		        if (!process.waitFor(1, TimeUnit.MINUTES)) {
		            System.err.println("프로세스 실행 실패, 타임아웃");
		            process.destroy(); // 프로세스 강제 종료
		            return false;
		        }

		        int exitCode = process.exitValue();
		        if (exitCode == 0) {
		            System.out.println("프로세스 실행 성공");
		            vo.setFilename("F_"+vo.getFilename());
		            fileDAO.filterFiles(vo);
		        } else {
		            System.err.println("프로세스 실행 실패, 종료 코드: " + exitCode);
		        }
		    } catch (IOException | InterruptedException e) {
		        e.printStackTrace();
		    }
		    return false;
	}

	
	
	
	// WAV 파일에서 오디오 샘플 로드
		private static ArrayList<Float> loadAudioSamples(String filePath) throws IOException, UnsupportedAudioFileException {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
			int numChannels = audioInputStream.getFormat().getChannels();
			int frameSize = audioInputStream.getFormat().getFrameSize();
			int bufferSize = frameSize / numChannels;
			byte[] buffer = new byte[bufferSize];
			ArrayList<Float> audioSamples = new ArrayList<>();
			while (audioInputStream.read(buffer) != -1) {
				for (int i = 0; i < bufferSize; i += 2) {
					short sample = (short) ((buffer[i + 1] << 8) | (buffer[i] & 0xFF));
					float sampleValue = sample / 32768.0f;
					audioSamples.add(sampleValue);
				}
			}
			return audioSamples;
		}

		// WAV 파일의 샘플 레이트 반환
		private static double getSampleRate(String filePath) throws IOException, UnsupportedAudioFileException {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
			return audioInputStream.getFormat().getSampleRate();
		}

		// 오디오 샘플을 WAV 파일로 저장
		private static void saveAudioSamples(double[] audioSamples, String filePath, double sampleRate) throws IOException {
			AudioFormat format = new AudioFormat((float) sampleRate, 16, 1, true, false);
			byte[] outputBuffer = new byte[audioSamples.length * 2];
			for (int i = 0; i < audioSamples.length; i++) {
				short sampleValue = (short) (audioSamples[i] * 32767);
				outputBuffer[i * 2] = (byte) (sampleValue & 0xFF);
				outputBuffer[i * 2 + 1] = (byte) ((sampleValue >> 8) & 0xFF);
			}
			AudioInputStream audioInputStream = new AudioInputStream(new ByteArrayInputStream(outputBuffer), format, audioSamples.length);
			AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File(filePath));
		}

		

}
