
package wisematches.client.android.widget;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.*;

/**
 * Copy of original ArrayAdapter but with more extensible solution.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ArrayAdapter<T> extends BaseAdapter implements Filterable {
	private boolean notifyOnChange = true;

	protected final Context context;
	protected final int viewResource;
	protected final int dropDownResource;

	private final Object lock = new Object();


	private List<T> objects;
	private ArrayList<T> originalValues;

	private ArrayFilter arrayFilter;
	protected LayoutInflater layoutInflater;

	public ArrayAdapter(Context context, int viewResource, int dropDownResource, boolean notifyOnChange, List<T> objects) {
		this.context = context;
		this.viewResource = viewResource;
		this.dropDownResource = dropDownResource;
		this.notifyOnChange = notifyOnChange;

		this.objects = objects;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void add(T object) {
		synchronized (lock) {
			if (originalValues != null) {
				originalValues.add(object);
			} else {
				objects.add(object);
			}
		}
		if (notifyOnChange) notifyDataSetChanged();
	}

	public void addAll(Collection<? extends T> collection) {
		synchronized (lock) {
			if (originalValues != null) {
				originalValues.addAll(collection);
			} else {
				objects.addAll(collection);
			}
		}
		if (notifyOnChange) notifyDataSetChanged();
	}

	@SuppressWarnings("unchecked")
	public void addAll(T... items) {
		synchronized (lock) {
			if (originalValues != null) {
				Collections.addAll(originalValues, items);
			} else {
				Collections.addAll(objects, items);
			}
		}
		if (notifyOnChange) notifyDataSetChanged();
	}

	public void insert(T object, int index) {
		synchronized (lock) {
			if (originalValues != null) {
				originalValues.add(index, object);
			} else {
				objects.add(index, object);
			}
		}
		if (notifyOnChange) notifyDataSetChanged();
	}

	public void remove(T object) {
		synchronized (lock) {
			if (originalValues != null) {
				originalValues.remove(object);
			} else {
				objects.remove(object);
			}
		}
		if (notifyOnChange) notifyDataSetChanged();
	}

	public void clear() {
		synchronized (lock) {
			if (originalValues != null) {
				originalValues.clear();
			} else {
				objects.clear();
			}
		}
		if (notifyOnChange) notifyDataSetChanged();
	}

	public void sort(Comparator<? super T> comparator) {
		synchronized (lock) {
			if (originalValues != null) {
				Collections.sort(originalValues, comparator);
			} else {
				Collections.sort(objects, comparator);
			}
		}
		if (notifyOnChange) notifyDataSetChanged();
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		notifyOnChange = true;
	}

	public void setNotifyOnChange(boolean notifyOnChange) {
		this.notifyOnChange = notifyOnChange;
	}

	public int getCount() {
		return objects.size();
	}

	public T getItem(int position) {
		return objects.get(position);
	}

	public int getPosition(T item) {
		return objects.indexOf(item);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, viewResource);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, dropDownResource);
	}

	protected View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
		View view;
		if (convertView == null) {
			view = layoutInflater.inflate(resource, parent, false);
		} else {
			view = convertView;
		}
		populateValueToView(view, getItem(position));
		return view;
	}

	protected void populateValueToView(View view, T value) {
		defaultPopulateValueToView(view, value);
	}

	protected void defaultPopulateValueToView(View view, Object value) {
		TextView text;

		try {
			text = (TextView) view;
		} catch (ClassCastException e) {
			Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
			throw new IllegalStateException("ArrayAdapter requires the resource ID to be a TextView", e);
		}

		if (value == null) {
			text.setText("");
		} else if (value instanceof CharSequence) {
			text.setText((CharSequence) value);
		} else {
			text.setText(value.toString());
		}
	}

	public Filter getFilter() {
		if (arrayFilter == null) {
			arrayFilter = new ArrayFilter();
		}
		return arrayFilter;
	}

	private class ArrayFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

			if (originalValues == null) {
				synchronized (lock) {
					originalValues = new ArrayList<>(objects);
				}
			}

			if (prefix == null || prefix.length() == 0) {
				ArrayList<T> list;
				synchronized (lock) {
					list = new ArrayList<>(originalValues);
				}
				results.values = list;
				results.count = list.size();
			} else {
				String prefixString = prefix.toString().toLowerCase();

				ArrayList<T> values;
				synchronized (lock) {
					values = new ArrayList<>(originalValues);
				}

				final ArrayList<T> newValues = new ArrayList<>();
				for (final T value : values) {
					final String valueText = value.toString().toLowerCase();

					if (valueText.startsWith(prefixString)) {
						newValues.add(value);
					} else {
						final String[] words = valueText.split(" ");
						for (String word : words) {
							if (word.startsWith(prefixString)) {
								newValues.add(value);
								break;
							}
						}
					}
				}

				results.values = newValues;
				results.count = newValues.size();
			}

			return results;
		}

		@Override
		@SuppressWarnings("unchecked")
		protected void publishResults(CharSequence constraint, FilterResults results) {
			objects = (List<T>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	}
}
