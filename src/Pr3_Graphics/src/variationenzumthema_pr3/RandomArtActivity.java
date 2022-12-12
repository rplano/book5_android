package variationenzumthema_pr3;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.view.View;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * RandomArtActivity
 * 
 * If we compute the value at any point with −1 ≤ x, y ≤ 1, the result will also
 * be between −1 and 1. By scaling the answer to a grayscale value 0 to 255, we
 * can plot the function in this 2-by-2 square. (From three such expressions, we
 * can get red, green, and blue values for each point.) Surprisingly, every
 * sufficiently complicated expression has a decent chance of being interesting!
 * We can generate deeply-nested expressions completely randomly, see how they
 * look, and keep the good ones.
 * 
 * Based on http://nifty.stanford.edu/2009/stone-random-art/sml/index.html
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class RandomArtActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		RandomArtView gv = new RandomArtView(this);
		setContentView(gv);
	}

	class RandomArtView extends View {
		// needed for drawing
		private int mCanvasWidth;
		private int mCanvasHeight;
		private Bitmap bitmap;
		private int[] bitMapArray;

		public RandomArtView(final Context context) {
			super(context);

			setOnTouchListener(new OnSwipeListener(context) {
				public void onSwipeRight() {
					Toast.makeText(context, "saving...", Toast.LENGTH_SHORT).show();

					saveAsPNG();
				}

				public void onSwipeLeft() {
					Toast.makeText(context, "calculating...", Toast.LENGTH_SHORT).show();

					// needed for toast to become visible:
					Thread.yield();
					pause(100);
					Thread.yield();

					// do a redraw:
					postInvalidate();
				}
			});
		}

		private void saveAsPNG() {
			try {
				int time = (int) ((System.currentTimeMillis() / 1000) % 1000000);
				OutputStream stream = new FileOutputStream("/sdcard/RandomArt_" + time + ".png");
				bitmap.compress(CompressFormat.PNG, 0, stream);
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void onDraw(Canvas canvas) {
			int PIXEL_SIZE = 4;
			float width = getWidth() / PIXEL_SIZE;
			float height = getHeight() / PIXEL_SIZE;

			Paint paint = new Paint();
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.WHITE);

			String postfix = findGoodPostfix();

			float stepx = 2.0f / width;
			float stepy = 2.0f / height;
			for (float x = -1.0f; x < 1.0f; x += stepx) {
				for (float y = -1.0f; y < 1.0f; y += stepy) {
					int i = (int) ((x + 1.0) * width * PIXEL_SIZE) / 2;
					int j = (int) ((y + 1.0) * height * PIXEL_SIZE) / 2;

					double color01 = evaluate(postfix, x, y);
					int color255 = (int) ((color01 + 1.0) * 255) / 2;
					int colorInt = Color.rgb(color255, color255, color255);
					paint.setColor(colorInt);
					// canvas.drawPoint(i, j, paint);
					// canvas.drawRect(i, j, i + PIXEL_SIZE, j + PIXEL_SIZE,
					// paint);\
					// bitMapArray[j * mCanvasWidth + i] = colorInt;
					int len = bitMapArray.length;
					for (int ii = 0; ii < PIXEL_SIZE; ii++) {
						for (int jj = 0; jj < PIXEL_SIZE; jj++) {
							int idx = (j + jj) * mCanvasWidth + (i + ii);
							if (idx < len) {
								// bitMapArray[(y + j) * mCanvasWidth + (x + i)]
								// = color;
								bitMapArray[idx] = colorInt;
							}
						}
					}

				}
			}
			bitmap.setPixels(bitMapArray, 0, mCanvasWidth, 0, 0, mCanvasWidth, mCanvasHeight);

			// draw bitmap on canvas
			canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
					new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), null);
		}

		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			if (bitmap != null) {
				bitmap.recycle();
			}
			bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			mCanvasWidth = w;
			mCanvasHeight = h;
			bitMapArray = new int[mCanvasWidth * mCanvasHeight];
		}

		public void destroy() {
			if (bitmap != null) {
				bitmap.recycle();
			}
		}

		private String findGoodPostfix() {
			String infix = "";
			while (true) {
				infix = createMathFunction();
				if ((infix.length() > 50) && (infix.length() < 200))
					break;
			}
			String postfix = convertFromInfixToPostfix(infix);
			return postfix;
		}

		/**
		 * Evaluates a postfix expression. The rules are: <br/>
		 * 1) read the tokens one at a time; <br/>
		 * 2) if it is an 'x' or a 'y', push it on the stack; <br/>
		 * 3) if it is a 'c', 's', '*' or ',' then pop the top two elements from
		 * the stack, apply the operator to the two elements, and push the
		 * result back on the stack. <br/>
		 * At the end the last element on the stack is the results.
		 * 
		 * @param postfix
		 *            xyycy,s*yc,cx,x,cys,*yc,
		 */
		private double evaluate(String postfix, double x, double y) {
			Stack<Double> st = new Stack<Double>();
			for (int i = 0; i < postfix.length(); i++) {
				char c = postfix.charAt(i);
				switch (c) {
				case 'x':
					st.push(x);
					break;
				case 'y':
					st.push(y);
					break;
				case 'c':
					double s = st.pop();
					double r = Math.cos(Math.PI * s);
					st.push(r);
					break;
				case 's':
					s = st.pop();
					r = Math.sin(Math.PI * s);
					st.push(r);
					break;
				case ',':
					double s1 = st.pop();
					double s2 = st.pop();
					r = (s1 + s2) / 2.0;
					st.push(r);
					break;
				case '*':
					s1 = st.pop();
					s2 = st.pop();
					r = s1 * s2;
					st.push(r);
					break;
				default:
					Log.i("RandomArtActivity", "we should not get here: " + c);
					break;
				}
			}

			return st.pop();
		}

		/**
		 * Converts am infix expression into a postfix expression. The rules
		 * are: <br/>
		 * 1) read the tokens one at a time; <br/>
		 * 2) if it is an '(' do nothing; <br/>
		 * 3) if it is a '(' pop the top-most element from the stack and add it
		 * to the out string; <br/>
		 * 4) if it is a 'c', 's', '*' or ',' push it on the stack; <br/>
		 * 5) if it is an 'x' or a 'y', add it to the out string; <br/>
		 * Return the out string, which is the postfix expression.
		 * 
		 * @param infix
		 *            ((x*(c(((c(((y*s((c(y),y))),c(y))),x),x)),s(y))),c(y))
		 * @return xyycy,s*yc,cx,x,cys,*yc,
		 */
		private String convertFromInfixToPostfix(String infix) {
			Stack<String> st = new Stack<String>();
			String out = "";
			for (int i = 0; i < infix.length(); i++) {
				char c = infix.charAt(i);
				switch (c) {
				case '(':
					break;
				case ')':
					out += "" + st.pop();
					break;
				case ',':
				case '*':
				case 's':
				case 'c':
					st.push("" + c);
					break;
				case 'x':
				case 'y':
					out += "" + c;
					break;
				default:
					Log.i("RandomArtActivity", "we should not get here: " + c);
					break;
				}
			}

			return out;
		}

		/**
		 * A recursive method, that generates a mathematical expression,
		 * consisting of sin(), cos(), multiplication and average of two
		 * numbers.
		 */
		private String createMathFunction() {
			String s = "";
			int key = (int) (Math.random() * 6);
			switch (key) {
			case 0:
				s = "x";
				break;
			case 1:
				s = "y";
				break;
			case 2:
				// cosinus
				s = "c(" + createMathFunction() + ")";
				break;
			case 3:
				// sinus
				s = "s(" + createMathFunction() + ")";
				break;
			case 4:
				// multiplication
				s = "(" + createMathFunction() + "*" + createMathFunction() + ")";
				break;
			case 5:
				// average:
				s = "(" + createMathFunction() + "," + createMathFunction() + ")";
				break;
			}
			return s;
		}
	}

	private void pause(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}
}
