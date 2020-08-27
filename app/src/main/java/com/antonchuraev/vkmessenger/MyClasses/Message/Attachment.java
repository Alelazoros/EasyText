package com.antonchuraev.vkmessenger.MyClasses.Message;

public class Attachment {
	public AttachmentType attachmentType;
	public Object attachment;

	@Override
	public String toString() {
		return "Attachment{" +
				"attachmentType=" + attachmentType +
				", attachment=" + attachment +
				'}';
	}

	enum AttachmentType { //TODO ALL TYPES
		PHOTO,
		AUDIO,
		LINK,
		WALL,
		VOICE_MESSAGE,
		CALL,
		STICKER,
		FORWARDED_MESSAGE
	}
}
