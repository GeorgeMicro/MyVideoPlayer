package com.videoplayer.main;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.SwingWorker;

import com.videoplayer.views.MainWindow;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

public class PlayerMain {

	static MainWindow frame;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean found = new NativeDiscovery().discover();
		System.out.println(found);
		System.out.println(LibVlc.INSTANCE.libvlc_get_version());

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new MainWindow();
					frame.setVisible(true);
					frame.getMediaPlayer().prepareMedia(
							"C:\\Users\\Yuxuan\\Desktop\\Donald Trump Interviews Himself In the Mirror.mp4");
					new SwingWorker<String, Integer>() {

						@Override
						protected String doInBackground() throws Exception {
							// TODO Auto-generated method stub
							while (true) {
								long total = frame.getMediaPlayer().getLength();
								long curr = frame.getMediaPlayer().getTime();
								float percent  = 100 * (float)curr / total;
								publish((int)percent);
								Thread.sleep(100);
							}
						}

						protected void process(java.util.List<Integer> chunks) {
							for (int v : chunks) {
								frame.getProgressBar().setValue(v);
							}

						}
					}.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void play() {
		frame.getMediaPlayer().play();
	}

	public static void pause() {
		frame.getMediaPlayer().pause();
	}

	public static void stop() {
		frame.getMediaPlayer().stop();
	}

	public static void jumpTo(float to) {
		frame.getMediaPlayer().setTime((long) (to * frame.getMediaPlayer().getLength()));
	}
	
	public static void setVolume(int v) {
		frame.getMediaPlayer().setVolume(v);
	}
	public static void openVideo() {
		JFileChooser chooser = new JFileChooser();
		int v = chooser.showOpenDialog(null);
		if (v == JFileChooser.APPROVE_OPTION)
		{
			File file = chooser.getSelectedFile();
			frame.getMediaPlayer().playMedia(file.getAbsolutePath());
		}
	}
	
	public static void openSubtitle() {
		JFileChooser chooser = new JFileChooser();
		int v = chooser.showOpenDialog(null);
		if (v == JFileChooser.APPROVE_OPTION)
		{
			File file = chooser.getSelectedFile();
			frame.getMediaPlayer().setSubTitleFile(file);
		}
	}
	
	public static void exit() {
		frame.getMediaPlayer().release();
		System.exit(0);
	}
}
