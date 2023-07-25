module kakurojavafx {
	requires javafx.controls;
	requires javafx.fxml;
	requires transitive javafx.graphics;
	requires javafx.base;

	opens kakurofx to javafx.fxml;
	exports kakurofx;
}
