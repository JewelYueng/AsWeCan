package org.k2.processmining.support.normal.transform;

	public class FormatItem {
		public char placeHolder; //占位符
		public int position; //占位符在模板中的位置
		public String placeHolderString;//占位符所代表的字符串
		public char getPlaceHolder() {
			return placeHolder;
		}
		public void setPlaceHolder(char placeHolder) {
			this.placeHolder = placeHolder;
		}
		public int getPosition() {
			return position;
		}
		public void setPosition(int position) {
			this.position = position;
		}
		
		public String getPlaceHolderString() {
			return placeHolderString;
		}
		public void setPlaceHolderString(String placeHolderString) {
			this.placeHolderString = placeHolderString;
		}
		public FormatItem(char placeHolder, int position) {
			super();
			this.placeHolder = placeHolder;
			this.position = position;
			this.placeHolderString = "null";
		}
		@Override
		public String toString() {
			return "FormatItem [placeHolder=" + placeHolder + ", position="
					+ position + ", placeHolderString=" + placeHolderString
					+ "]";
		}

		
}
