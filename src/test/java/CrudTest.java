

import services.impl.CRUDImpl;
import services.impl.DatabaseImpl;

public class CrudTest {

	public static void main(String[] args) {
		CRUDImpl crud = new CRUDImpl(new DatabaseImpl());

	}

}
