package inference.qualifyingFunction;

import java.util.ArrayList;

@SuppressWarnings("serial")
class FrequecySortTable extends ArrayList<LineTable> {
	ArrayList<Integer> indexs0s; // indices sem observaciones

	public FrequecySortTable() {
		super();
		this.indexs0s = new ArrayList<>();
	}

	public FrequecySortTable(int[][] frequecyArray) {
		super(frequecyArray.length);
		this.indexs0s = new ArrayList<>();
		for (int index = 0; index < frequecyArray.length; index++) {
			LineTable lt = new LineTable(frequecyArray[index], index);
			if (lt.sum > 0)
				this.add(lt);
			else
				this.indexs0s.add(index);
		}
	}

	@Override
	public boolean add(LineTable e) {
		// if(isWhitFrequencytable && e.sum==0) {
		// indexs0s.add(e.originalIndex);return true;
		// }
		int start = 0;
		int end = this.size() - 1;
		while (start <= end) {
			int mid = (start + end) / 2;
			if (this.get(mid).compareTo(e) == 0) {
				super.add(mid, e);
				return true;
			}
			if (this.get(mid).compareTo(e) < 0) {
				end = mid - 1;
			} else {
				start = mid + 1;
			}
		}
		super.add(start, e);
		return true;
	}
}
