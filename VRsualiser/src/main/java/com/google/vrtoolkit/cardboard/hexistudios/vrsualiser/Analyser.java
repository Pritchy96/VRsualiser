package com.google.vrtoolkit.cardboard.hexistudios.vrsualiser;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.audiofx.Visualizer;

import com.google.vrtoolkit.cardboard.hexistudios.vrsualiser.renderers.Renderer;

/**
 * Created by Tom on 27/03/2016.
 */
public class Analyser {
  private Visualizer visualiser;
  private AudioRecord audioRecorder;
  private static final int RECORDING_SAMPLE_RATE = 44100;
  Renderer renderer;

  public Analyser(Renderer renderer) {
    this.renderer = renderer;

    renderer.setup(new Object[]{Visualizer.getCaptureSizeRange()[1], 256});

    //Setup the visualiser, choosing whether to use audio from another app or microphone.
    setupVisualiser(false, true, true); //True: Use microphone, collect waveform data, convert to fft.
  }

  public void onPause(boolean isFinishing) {
    if (isFinishing) {
      visualiser.release();
    }
  }

  public void onDestroy() {
    visualiser.release();
  }

  private void initMicrophone() {
    int bufferSize = AudioRecord.getMinBufferSize(
        RECORDING_SAMPLE_RATE,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    );

    audioRecorder = new AudioRecord(
        MediaRecorder.AudioSource.MIC,
        RECORDING_SAMPLE_RATE,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT,
        bufferSize
    );
    //audioRecorder.startRecording();
  }

  private void setupVisualiser(Boolean useMicrophone, Boolean getWaveform, Boolean getFft) {
    // Create the Visualizer object and attach it to our input.
    if (useMicrophone) {
      initMicrophone();
      visualiser = new Visualizer(audioRecorder.getAudioSessionId());
    } else {
      visualiser = new Visualizer(0);
    }

    visualiser.setCaptureSize(Visualizer.getCaptureSizeRange()[1]); //Set capture size to max.

    //Setup listener to fill bytes with waveform/fft data every sampleRate time.
    visualiser.setDataCaptureListener(
        new Visualizer.OnDataCaptureListener() {
          public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
            renderer.updateVisualiserWave(bytes);
          }

          public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
            renderer.updateVisualiserFft(bytes);
          }
        }, Visualizer.getMaxCaptureRate() / 2, getWaveform, getFft);  //Choose to get waveform, fft output.
    visualiser.setEnabled(true); //Enabled only when needed, after setCaptureSize.
  }
}