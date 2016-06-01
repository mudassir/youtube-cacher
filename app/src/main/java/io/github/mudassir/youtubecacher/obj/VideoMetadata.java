package io.github.mudassir.youtubecacher.obj;

/**
 * Object for video metadata with builder
 */
public class VideoMetadata {

	public static class Builder {

		private String title;
		private String id;
		private String duration;
		private String postedTime;
		private String views;
		private String channel;
		private String channelThumbnail;

		public Builder() { }

		public Builder title(String title) {
			this.title = title;
			return this;
		}

		public Builder id(String id) {
			this.id = id;
			return this;
		}

		public Builder duration(String duration) {
			this.duration = duration;
			return this;
		}

		public Builder postedTime(String postedTime) {
			this.postedTime = postedTime;
			return this;
		}

		public Builder views(String views) {
			this.views = views;
			return this;
		}

		public Builder channel(String channel) {
			this.channel = channel;
			return this;
		}

		public Builder channelThumbnail(String channelThumbnail) {
			this.channelThumbnail = channelThumbnail;
			return this;
		}

		public VideoMetadata build() {
			return new VideoMetadata(title, id, duration, postedTime, views, channel, channelThumbnail);
		}
	}

	private String title;
	private String id;
	private String duration;
	private String postedTime;
	private String views;
	private String channel;
	private String channelThumbnail;

	public VideoMetadata(String title, String id, String duration, String postedTime, String views, String channel, String channelThumbnail) {
		this.title = title;
		this.id = id;
		this.duration = duration;
		this.postedTime = postedTime;
		this.views = views;
		this.channel = channel;
		this.channelThumbnail = channelThumbnail;
	}

	public String getTitle() {
		return title;
	}

	public String getId() {
		return id;
	}

	public String getDuration() {
		return duration;
	}

	public String getPostedTime() {
		return postedTime;
	}

	public String getViews() {
		return views;
	}

	public String getChannel() {
		return channel;
	}

	public String getChannelThumbnail() {
		return channelThumbnail;
	}
}
