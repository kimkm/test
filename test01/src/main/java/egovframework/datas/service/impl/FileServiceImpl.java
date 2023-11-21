package egovframework.datas.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
