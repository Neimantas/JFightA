package models.constant;

public enum ImageType {

	JPG(".jpg"), PNG(".png"), GIF(".gif");

	private String _imageExtension;

	private ImageType(String imageExtension) {
		_imageExtension = imageExtension;
	}

	public static ImageType getByImageExtension(String imageExtension) {
		for (ImageType imageType : ImageType.values()) {
			if (imageType.getImageExtension().equalsIgnoreCase(imageExtension)
					|| imageType.getImageExtension().equalsIgnoreCase("." + imageExtension)) {
				return imageType;
			}
		}
		return null;
	}

	public String getImageExtension() {
		return _imageExtension;
	}

}
