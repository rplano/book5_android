package variationenzumthema_ch3;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * GraphicsActivity
 *
 * This activity shows the most common drawing methods using a View.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class GraphicsActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new GraphicsView(this));
	}

	private class GraphicsView extends View {

		public GraphicsView(Context context) {
			super(context);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			setBackgroundColor(0x200000ff);

			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setColor(Color.BLACK);
			paint.setTextSize(64f);
			canvas.drawText("Hello World!", getWidth() / 2 - 180, 200, paint);

			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(Color.WHITE);
			paint.setStrokeWidth(8);
			canvas.drawLine(getWidth() / 2 - 200, 210, getWidth() / 2 + 200, 210, paint);

			float x = 200;
			float y = 400;
			float w = 100;
			float h = 100;
			paint.setColor(Color.BLUE);
			canvas.drawRect(x, y, x + w, y + h, paint);

			x += 200;
			paint.setStyle(Paint.Style.FILL);
			canvas.drawRect(x, y, x + w, y + h, paint);

			x += 200;
			paint.setColor(Color.GREEN);
			canvas.drawOval(new RectF(x, y, x + w, y + h), paint);

			x += 200;
			paint.setColor(Color.GREEN);
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawOval(new RectF(x, y, x + w, y + h), paint);

			x = 200;
			y += 200;
			float[] pts = { 200, 650, 225, 600, 275, 600, 300, 650, 275, 700, 225, 700 };
			paint.setColor(Color.RED);
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawLines(pts, paint);

			x += 200;
			float[] pts2 = { 400, 650, 425, 600, 475, 600, 500, 650, 475, 700, 425, 700 };
			canvas.drawPoints(pts2, paint);

			x += 200;
			paint.setColor(Color.CYAN);
			paint.setStyle(Paint.Style.FILL);
			RectF mRectF = new RectF(x, y, x + w, y + h);
			float startAngle = 45;
			float sweepAngle = 270;
			canvas.drawArc(mRectF, startAngle, sweepAngle, true, paint);

			x += 200;
			paint.setStyle(Paint.Style.STROKE);
			mRectF = new RectF(x, y, x + w, y + h);
			canvas.drawArc(mRectF, startAngle, sweepAngle, true, paint);

			x = 200;
			y += 250;
			Path path = new Path();
			path.reset();
			path.moveTo(x, y);
			for (int i = 0; i < 7; i++) {
				float x0 = x;
				x += 100;
				float y0 = y;
				y += ((i % 2) - 0.5) * 200;
				// path.lineTo(x, y);
				path.quadTo(x, y, x0, y0);
			}
			// path.close();
			paint.setColor(Color.YELLOW);
			canvas.drawPath(path, paint);

			x = 200;
			y += 200;
			try {
				InputStream is = getAssets().open("Mona_Lisa.jpg");
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				canvas.drawBitmap(bitmap, x, y, null);
			} catch (IOException e) {
				Log.e("GraphicsActivity", e.getMessage());
			}
		}
	}

}