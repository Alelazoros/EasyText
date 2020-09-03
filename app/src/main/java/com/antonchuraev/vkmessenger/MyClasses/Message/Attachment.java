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
		WALL, //TODO VIDEO
		VIDEO,
		VOICE_MESSAGE, //TODO СДЕЛАТЬ ПРОСЛУШИВАНИЕ НО НЕ ОТПРАВЛЕНИЕ
		CALL, //TODO INCOMING
		STICKER, //TODO CANT FIND
		FORWARDED_MESSAGE //TODO ПЕРЕСЛАННОЕ СООБЩЕНИЕ
		//TODO ФАЙЛ
	}
}
