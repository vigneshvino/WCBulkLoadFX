package application;

public final class FOHolder {

	private FO fo;
	private final static FOHolder INSTANCE = new FOHolder();
	
	private FOHolder() {}
	
	public static FOHolder getInstance() {
		return INSTANCE;
	}

	public FO getFo() {
		return fo;
	}

	public void setFo(FO fo) {
		this.fo = fo;
	}
	
}
