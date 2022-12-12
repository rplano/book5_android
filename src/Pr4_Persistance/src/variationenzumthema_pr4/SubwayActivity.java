package variationenzumthema_pr4;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import algorithms.graph.Dijkstra;
import algorithms.graph.GraphEdgeList;
import algorithms.graph.Vertex;
import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import variationenzumthema.pr4.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * SubwayActivity
 * 
 * Finds a connection between two subway stations using Dijkstra algorithm.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class SubwayActivity extends Activity {

	private TextView tv;

	private GraphEdgeList<Integer, String> graph;
	private Map<String, Vertex<String>> vertices;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		loadSubwayGraphFromFile("subway_nbg.txt");

		setContentView(R.layout.subway_activity);

		List<String> stations = new ArrayList<String>(vertices.keySet());
		Collections.sort(stations);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stations);
		final Spinner spFrom = (Spinner) findViewById(R.id.spinner1);
		spFrom.setAdapter(adapter);

		final Spinner spTo = (Spinner) findViewById(R.id.spinner2);
		spTo.setAdapter(adapter);

		Button btn = (Button) findViewById(R.id.button);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String from = (String) spFrom.getSelectedItem();
				String to = (String) spTo.getSelectedItem();
				findConnection(from, to);
			}

		});

		tv = (TextView) findViewById(R.id.textview);
		tv.setMovementMethod(new ScrollingMovementMethod());

	}

	private void findConnection(String from, String to) {
		Vertex<String> sourceVertex = getVertex(from);
		Vertex<String> destinationVertex = getVertex(to);
		try {
			if (sourceVertex != null && destinationVertex != null) {
				Dijkstra<Integer, String> dijk = new Dijkstra<Integer, String>(graph);

				String text = "Shortest distance: " + dijk.shortestPath(sourceVertex, destinationVertex) + "\n";

				// fastest route from B to A:
				text += "Fastest route from " + sourceVertex.getElement() + " to " + destinationVertex.getElement()
						+ ": \n";

				Map<Vertex<String>, Vertex<String>> predcrs = dijk.getAllPredecessors(sourceVertex);
				Vertex<String> vTmp = destinationVertex;
				while (vTmp != sourceVertex) {
					text += vTmp.getElement() + " -> \n";
					vTmp = predcrs.get(vTmp);
				}
				text += vTmp.getElement() + "\n";
				tv.setText(text);
				Log.i("SubwayActivity", text);
			}

		} catch (Exception e) {
			Log.e("SubwayActivity", e.getMessage());
		}
	}

	private Vertex<String> getVertex(String source) {
		while (true) {
			Vertex<String> sourceVertex = graph.findVertex(source);
			if (sourceVertex != null) {
				return sourceVertex;
			}
		}
	}

	private void loadSubwayGraphFromFile(String fileName) {
		graph = new GraphEdgeList<Integer, String>();
		vertices = new HashMap<String, Vertex<String>>();

		try {
			InputStream is = getAssets().open(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				if (!line.startsWith("#")) { // ignore comments
					analyze(line);
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void analyze(String line) {
		StringTokenizer st = new StringTokenizer(line, ",:", false);
		String subwayLine = st.nextToken();

		String from = st.nextToken().trim();
		addVertexToGraph(from);

		while (st.hasMoreTokens()) {
			String to = st.nextToken().trim();
			addVertexToGraph(to);

			// all subway station are separated by 1 mile (or 1 minute)
			graph.insertEdge(vertices.get(from), vertices.get(to), 1);

			from = to;
		}
	}

	private void addVertexToGraph(String from) {
		if (!vertices.containsKey(from)) {
			Vertex<String> vtx = graph.insertVertex(new Vertex<String>(from));
			vertices.put(from, vtx);
		}
	}
}