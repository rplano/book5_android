package variationenzumthema_pr4;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import algorithms.graph.AbstractEdge;
import algorithms.graph.GraphEdgeList;
import algorithms.graph.GraphParser;
import algorithms.graph.Vertex;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import variationenzumthema.pr4.R;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * Synonyms
 * 
 * A console program, used for demonstrating navigation through a graph.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class SynonymsActivity extends Activity {

	private EditText et;
	private ListView lv;
	private List<String> synonyms;

	private GraphEdgeList<String, String> graph;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initSynonymsGraph();
		synonyms = new ArrayList<String>();
		setContentView(R.layout.synonyms_activity);

		et = (EditText) findViewById(R.id.edittext);

		Button btn = (Button) findViewById(R.id.button);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				followSynonym(v);
			}
		});

		lv = (ListView) findViewById(R.id.listview);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, synonyms);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String word = synonyms.get(position);
				et.setText(word);
				followSynonym(view);
			}
		});
	}

	private void initSynonymsGraph() {
		try {
			InputStream is = getAssets().open("synonyms.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			graph = new GraphParser().parseGraph(br);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<String> findSynonyms(Vertex<String> currentVertex) {
		List<String> roomsToGo = new ArrayList<String>();
		Collection<AbstractEdge<String>> outEdgs = graph.incidentEdges(currentVertex);
		for (AbstractEdge<String> edge : outEdgs) {
			roomsToGo.add(graph.opposite(currentVertex, edge).getElement());
		}
		return roomsToGo;
	}

	private void followSynonym(View v) {
		String word = et.getText().toString();
		Vertex<String> currentVertex = graph.findVertex(word);
		synonyms = findSynonyms(currentVertex);
		if (synonyms.size() > 0) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(),
					android.R.layout.simple_spinner_item, synonyms);
			lv.setAdapter(adapter);
		} else {
			Toast.makeText(v.getContext(), "There are no synonyms for: " + word, Toast.LENGTH_LONG).show();
		}
	}
}