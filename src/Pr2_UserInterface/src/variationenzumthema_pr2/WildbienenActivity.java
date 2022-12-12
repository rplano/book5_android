package variationenzumthema_pr2;

import algorithms.tree.BinaryNode;
import algorithms.tree.BinaryTree;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import variationenzumthema.pr2.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * WildBienen
 * 
 * Based on handout about wild bees. Uses decision trees from second book.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class WildbienenActivity extends Activity {

	private BinaryTree<String> decisions;
	private BinaryNode<String> currentNode;
	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wildbienen_activity);

		tv = (TextView) findViewById(R.id.question);

		Button btnYes = (Button) findViewById(R.id.btn_yes);
		btnYes.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				walkThrough("yes");
			}
		});

		Button btnNo = (Button) findViewById(R.id.btn_no);
		btnNo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				walkThrough("yes");
			}
		});

		createDecisionTree();
		currentNode = (BinaryNode<String>) decisions.root();
		tv.setText(currentNode.getElement());
	}

	private void walkThrough(String anwser) {
		if (anwser.toLowerCase().equals("yes")) {
			currentNode = currentNode.getLeft();
		} else {
			currentNode = currentNode.getRight();
		}
		tv.setText(currentNode.getElement());
	}

	private void createDecisionTree() {
		// create decision tree, left is yes:
		BinaryNode<String> root = new BinaryNode<String>(
				"Dichte Haarbüschel (\"Sammelbürsten\") am 3. Beinpaar oder auf der Unterseite des Hinterleibes?");

		BinaryNode<String> wheels = new BinaryNode<String>("Wheels attached?");
		root.setLeft(wheels);
		root.setRight(new BinaryNode<String>("Don't Fly!"));

		BinaryNode<String> water = new BinaryNode<String>("Water plane?");
		wheels.setLeft(new BinaryNode<String>("Fly!"));
		wheels.setRight(water);

		water.setLeft(new BinaryNode<String>("Fly!"));
		water.setRight(new BinaryNode<String>("Don't Fly!"));

		decisions = new BinaryTree<String>(root);
	}
}