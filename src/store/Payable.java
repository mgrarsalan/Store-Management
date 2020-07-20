package store;
import java.io.IOException;

public interface Payable {
	public float calculatePayment() throws IOException;
}
