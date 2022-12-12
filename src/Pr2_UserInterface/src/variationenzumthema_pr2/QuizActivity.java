package variationenzumthema_pr2;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * QuizActivity
 * 
 * An activity that creates a UI for multiple choice exams. The questions come
 * from a string array, the UI is created dynamically.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class QuizActivity extends Activity {

	private final String[] QUESTIONS = { 
			"Correct:  1 + 1 = 2 ?; Yes; No; Maybe", 
			"What is 2 ^ 2 ?; 2; 4; 8",
			"A zebra has stripes?; Yes; No" };

	private List<Question> questions;
	private int currentQuestion = 0;

	private LinearLayout llQuestion;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		loadQuestions();
		setContentView(createUI());
	}

	private void loadQuestions() {
		questions = new ArrayList<Question>();
		for (int i = 0; i < QUESTIONS.length; i++) {
			String[] words = QUESTIONS[i].split(";");
			Question q = new Question(words);
			questions.add(q);
		}
	}

	private View createUI() {
		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundColor(0x200000ff);
		ll.setOrientation(LinearLayout.VERTICAL);

		llQuestion = createQuestionView(questions.get(currentQuestion));
		ll.addView(llQuestion);
		
		ll.addView(createNavigationView());

		return ll;
	}

	private LinearLayout createQuestionView(Question q) {
		LinearLayout ll2 = new LinearLayout(this);
		ll2.setOrientation(LinearLayout.VERTICAL);

		TextView tvQuestion = new TextView(this);
		tvQuestion.setText(q.question);
		tvQuestion.setGravity(Gravity.LEFT);
		tvQuestion.setTextSize(24);
		ll2.addView(tvQuestion);

		RadioGroup rg = new RadioGroup(this);
		for (int i = 1; i < q.answers.length; i++) {
			RadioButton rb1 = new RadioButton(this);
			rb1.setTextSize(24);
			rb1.setText(q.answers[i]);
			rg.addView(rb1);
		}
		ll2.addView(rg);

		return ll2;
	}

	private View createNavigationView() {
		RelativeLayout rl = new RelativeLayout(this);
		rl.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		Button btnPrevious = new Button(this);
		btnPrevious.setTextSize(24);
		btnPrevious.setText("Prev");
		btnPrevious.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "Prev!", Toast.LENGTH_SHORT).show();
				if (currentQuestion > 0) {
					currentQuestion--;
					setContentView(createUI());
				}
			}
		});
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		btnPrevious.setLayoutParams(params1);
		btnPrevious.setGravity(Gravity.CENTER);

		Button btnNext = new Button(this);
		btnNext.setTextSize(24);
		btnNext.setText("Next");
		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "Next!", Toast.LENGTH_SHORT).show();
				if (currentQuestion < questions.size() - 1) {
					currentQuestion++;
					setContentView(createUI());
				}
			}
		});
		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		btnNext.setLayoutParams(params2);
		btnNext.setGravity(Gravity.CENTER);

		rl.addView(btnPrevious);
		rl.addView(btnNext);
		return rl;
	}
}

class Question {
	public String question;
	public String[] answers;

	public Question(String[] words) {
		question = words[0];
		answers = words;
	}
}