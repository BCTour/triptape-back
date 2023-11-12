package com.ssafy.triptape.file;

public class FileInfoDto {
	private String saveFolder;
	private String originalFile; // 원본 파일 이름
	private String saveFile; // 실제 저장 파일 이름

	public String getSaveFolder() {
		return saveFolder;
	}

	public void setSaveFolder(String saveFolder) {
		this.saveFolder = saveFolder;
	}

	public String getOriginalFile() {
		return originalFile;
	}

	public void setOriginalFile(String originalFile) {
		this.originalFile = originalFile;
	}

	public String getSaveFile() {
		return saveFile;
	}

	public void setSaveFile(String saveFile) {
		this.saveFile = saveFile;
	}
}
