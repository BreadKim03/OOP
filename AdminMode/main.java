package AdminMode;

import javax.swing.SwingUtilities;

import AdminMode.UI.AdminMenu;

public class main
{
	public main()
    {
        SwingUtilities.invokeLater(() -> new AdminMenu());
    }
}
