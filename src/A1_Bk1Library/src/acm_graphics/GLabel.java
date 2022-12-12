package acm_graphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

public class GLabel extends GObject {
	private String text;
	private Paint cPaint;

	public GLabel(String text) {
		this(text, 0, 0);
	}

	public GLabel(String text, int x, int y) {
		super(x, y, 0, 0);

		this.text = text;

		cPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		cPaint.setStrokeWidth(1);
		cPaint.setTextSize(48f);
		cPaint.setColor(color);
	}

	public void setLabel(String text) {
		this.text = text;
	}

	public String getLabel() {
		return this.text;
	}
	
	public int getAscent() {
		return 36;
	}

	public void setFont(String font) {
		// "Arial-bold-20"
		String[] parts = font.split("-");
		if (parts.length == 3) {
			// font size
			cPaint.setTextSize(Float.parseFloat(parts[2]));
			// font style
			int style = Typeface.NORMAL;
			switch (parts[1].toLowerCase()) {
			case "bold":
				style = Typeface.BOLD;
				break;
			case "italic":
				style = Typeface.ITALIC;
				break;
			}
			// font type
			Typeface type = Typeface.SANS_SERIF;
			switch (parts[2].toLowerCase()) {
			case "courier":
			case "monospace":
			case "monospaced":
				type = Typeface.MONOSPACE;
				break;
			case "times":
			case "times new roman":
			case "serif":
				type = Typeface.SERIF;
				break;
			}
			cPaint.setTypeface(Typeface.create(type, style));
		}
	}

	@Override
	public void draw(Canvas canvas) {
		cPaint.setColor(color);
		canvas.drawText(text, x, y, cPaint);
	}

}
