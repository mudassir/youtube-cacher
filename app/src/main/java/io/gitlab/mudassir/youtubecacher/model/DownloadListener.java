package io.gitlab.mudassir.youtubecacher.model;

/**
 * Callback to the MainActivity to initiate download
 */
public interface DownloadListener {
	void download(String id);
}
