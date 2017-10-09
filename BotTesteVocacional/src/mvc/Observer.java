package mvc;

import java.util.ArrayList;

public interface Observer {

	public void update(long chatId, String profissoes);

	public void update(Long chatId, ArrayList lista);
	
}
