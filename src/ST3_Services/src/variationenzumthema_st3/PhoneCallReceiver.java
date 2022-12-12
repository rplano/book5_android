package variationenzumthema_st3;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * PhoneCallReceiver
 *
 * This BroadcastReceiver records incoming phone calls.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class PhoneCallReceiver extends AbstractPhoneCallReceiver {

    @Override
    protected void onIncomingCallStarted(Context context, String number, Date start) {
		Toast.makeText(context, "onIncomingCallStarted(): "+number, Toast.LENGTH_LONG).show();
		recordAudio();
    }

    @Override
    protected void onOutgoingCallStarted(Context context, String number, Date start) {
		Toast.makeText(context, "onOutgoingCallStarted(): "+number, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onIncomingCallEnded(Context context, String number, Date start, Date end) {
		Toast.makeText(context, "onIncomingCallEnded(): "+number, Toast.LENGTH_LONG).show();
		playAudio();
    }

    @Override
    protected void onOutgoingCallEnded(Context context, String number, Date start, Date end) {
		Toast.makeText(context, "onOutgoingCallEnded(): "+number, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onMissedCall(Context context, String number, Date start) {
		Toast.makeText(context, "onMissedCall(): "+number, Toast.LENGTH_LONG).show();
    }
    
    private void playAudio() {
        Log.i("PhoneCallReceiver", "Playing audio...");
        int frequency = 8000;	//  44100, 22050, 16000, 11025 and 8000 may work
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;	// CHANNEL_IN_MONO is guaranteed to work on all devices.
        int audioEncod = AudioFormat.ENCODING_PCM_16BIT;
        File f = new File(Environment.getExternalStorageDirectory(), "raw.pcm" );

        try {
            BufferedInputStream bis = new BufferedInputStream( new FileInputStream(f) );
            DataInputStream dis = new DataInputStream( bis );

            int audioLength = (int) ( f.length() );
            short[] buffer = new short[audioLength];

            int i = 0;
            while ( dis.available() > 0 ) {
                buffer[i] = dis.readShort();
                i++;
            }

            dis.close();

            AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency, AudioFormat.CHANNEL_CONFIGURATION_MONO , AudioFormat.ENCODING_PCM_16BIT, audioLength, AudioTrack.MODE_STREAM);
            audioTrack.play();
            audioTrack.write(buffer, 0, audioLength);


        } catch (Exception e) {
            Log.i("PhoneCallReceiver", e.getMessage());
        }

    }

    private void recordAudio() {
        int frequency = 8000;	//  44100, 22050, 16000, 11025 and 8000 may work
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;	// CHANNEL_IN_MONO is guaranteed to work on all devices.
        int audioEncod = AudioFormat.ENCODING_PCM_16BIT;
        File f = new File(Environment.getExternalStorageDirectory(), "raw.pcm" );

        try {
            f.createNewFile();
        } catch (Exception e) {
            Log.i("PhoneCallReceiver", e.getMessage());
        }

        try {
            BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream(f) );
            DataOutputStream dos = new DataOutputStream( bos );

            int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfig, audioEncod);
            short[] buffer = new short[bufferSize];

            AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency, channelConfig, audioEncod, bufferSize);
            Log.i("PhoneCallReceiver", "Start recording...");
            audioRecord.startRecording();

            for (int i = 0; i < 100; i++) {
                int length = audioRecord.read(buffer, 0, bufferSize);
                for (int j = 0; j < length; j++) {
                    dos.writeShort(buffer[j]);
                }
            }

            audioRecord.stop();
            Log.i("PhoneCallReceiver", "finished recording.");
            dos.close();

        } catch (Exception e) {
            Log.i("PhoneCallReceiver", e.getMessage());
        }
    }
}
