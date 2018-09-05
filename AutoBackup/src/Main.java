import java.awt.AWTException;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;

public class Main {
	private static Thread thread;
	private static Time time;
	private static Thread times;
	private final static PopupMenu popup = new PopupMenu();
	private final static TrayIcon trayIcon = new TrayIcon(createImage("Auto Backup"));
	private final static SystemTray tray = SystemTray.getSystemTray();
	private static final String BACKUP = "D:\\EclipseBackup\\backup";
	private static final String WORKSPACE = "C:\\Users\\OFIR\\Documents\\eclipse-workspace";
	private static boolean state = true;
	private static long hours;
	private static long minutes;
	private static long interval;

	public static void main(String[] args) {
		init();
		run();
	}

	public static void init() {
		try {
			hours = Long.parseLong(JOptionPane.showInputDialog("Enter amount of hours between each check")) * 3600000;
			minutes = Long.parseLong(JOptionPane.showInputDialog("Enter amount of minutes between each check")) * 60000;
			interval = hours + minutes;
			time = new Time(interval);
			times = new Thread(time);
			times.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		createTrayMenu();
	}

	public static void run() {
		while (state) {
			if (calculateSize(Paths.get(WORKSPACE)) != calculateSize(Paths.get(BACKUP))) {
				copyFiles();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				trayIcon.displayMessage("All Ready!", "Nothing to copy, all backed up.", TrayIcon.MessageType.INFO);
				try {
					Thread.sleep(5000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				displayMessage();
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static long calculateSize(Path path) {
		try {
			if (Files.isRegularFile(path)) {
				return Files.size(path);
			}
			return Files.list(path).mapToLong(Main::calculateSize).sum();
		} catch (IOException e) {
			return -1;
		}
	}

	public static void displayMessage() {
		if (interval / 3600000 >= 1) {
			if (interval % 3600000 != 0) {
				if (interval / 3600000 < 2) {
					trayIcon.displayMessage("Sleeping...",
							"Checking again in " + interval / 3600000 + " hour and "
									+ (interval - (interval / 3600000) * 3600000) / 60000 + " minutes.",
							TrayIcon.MessageType.INFO);
				} else {
					trayIcon.displayMessage("Sleeping...",
							"Checking again in " + interval / 3600000 + " hours and "
									+ (interval - (interval / 3600000) * 3600000) / 60000 + " minutes.",
							TrayIcon.MessageType.INFO);
				}
			} else {
				if (interval / 3600000 < 2) {
					trayIcon.displayMessage("Sleeping...", "Checking again in " + interval / 3600000 + " hour.",
							TrayIcon.MessageType.INFO);
				} else {
					trayIcon.displayMessage("Sleeping...", "Checking again in " + interval / 3600000 + " hours.",
							TrayIcon.MessageType.INFO);
				}
			}
		} else {
			trayIcon.displayMessage("Sleeping...", "Checking again in " + interval / 60000 + " minutes.",
					TrayIcon.MessageType.INFO);
		}
	}

	public static void copyFiles() {
		File source = new File(WORKSPACE);
		File dest = new File(BACKUP);
		try {
			FileUtils.copyDirectory(source, dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Copied files Successfully!");
		trayIcon.displayMessage("Success!", "Copied files Successfully!", TrayIcon.MessageType.INFO);
	}

	private static void createTrayMenu() {
		// Check the SystemTray support
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}

		// Create a popup menu components
		MenuItem aboutItem = new MenuItem("About");
		Menu displayState = new Menu("ON/OFF");
		MenuItem power = new MenuItem("Turn OFF");
		Menu displayMenu = new Menu("Options");
		MenuItem setHours = new MenuItem("Set hours");
		MenuItem setMinutes = new MenuItem("Set minutes");
		MenuItem nextTest = new MenuItem("Next test");
		MenuItem exitItem = new MenuItem("Exit");
		trayIcon.setImageAutoSize(true);

		// Add components to popup menu
		popup.add(aboutItem);
		popup.add(displayState);
		displayState.add(power);
		popup.addSeparator();
		popup.add(displayMenu);
		displayMenu.add(setHours);
		displayMenu.add(setMinutes);
		displayMenu.add(nextTest);
		popup.add(exitItem);

		trayIcon.setPopupMenu(popup);

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
			return;
		}

		trayIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Simple auto backup program");
			}
		});

		aboutItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "@Ofir Pardo™");
			}
		});

		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuItem item = (MenuItem) e.getSource();
				if ("Set hours".toLowerCase().equals(item.getLabel().toLowerCase())) {
					try {
						hours = Long.parseLong(JOptionPane.showInputDialog("Enter amount of hours between each check"))
								* 3600000;
						interval = hours + minutes;
						time.setTime(interval);
						JOptionPane.showMessageDialog(null, "Hours has been set successfully");
						try {
							thread.interrupt();
						} catch (Exception ex) {
						}
						if (hours >= 1) {
							trayIcon.displayMessage("Sleeping...",
									"Checking again in " + interval / 3600000 + " hours and "
											+ (interval - (interval / 3600000) * 3600000) / 60000 + " minutes.",
									TrayIcon.MessageType.INFO);
						} else {
							trayIcon.displayMessage(
									"Sleeping...", "Checking again in "
											+ (interval - (interval / 3600000) * 3600000) / 60000 + " minutes.",
									TrayIcon.MessageType.INFO);
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Hours has not be set.");
					}

				} else if ("Set minutes".toLowerCase().equals(item.getLabel().toLowerCase())) {

					try {
						minutes = Long.parseLong(
								JOptionPane.showInputDialog("Enter amount of hours between each check")) * 60000;
						interval = hours + minutes;
						time.setTime(interval);
						JOptionPane.showMessageDialog(null, "minutes has been set successfully");
						try {
							thread.interrupt();
						} catch (Exception ex) {
						}
						if (hours >= 1) {
							trayIcon.displayMessage("Sleeping...",
									"Checking again in " + interval / 3600000 + " hours and "
											+ (interval - (interval / 3600000) * 3600000) / 60000 + " minutes.",
									TrayIcon.MessageType.INFO);
						} else {
							trayIcon.displayMessage(
									"Sleeping...", "Checking again in "
											+ (interval - (interval / 3600000) * 3600000) / 60000 + " minutes.",
									TrayIcon.MessageType.INFO);
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Hours has not be set.");
					}

				} else if ("Next test".toLowerCase().equals(item.getLabel().toLowerCase())) {
					if (interval / 3600000 >= 1) {
						trayIcon.displayMessage("Next test is in",
								time.getTime() / 3600000 + " hours and "
										+ (time.getTime() - (time.getTime() / 3600000) * 3600000) / 60000 + " minutes.",
								TrayIcon.MessageType.INFO);
					} else {
						trayIcon.displayMessage("Next test is in",
								+(time.getTime() - (time.getTime() / 3600000) * 3600000) / 60000 + " minutes.",
								TrayIcon.MessageType.INFO);
					}

				} else if ("Turn OFF".toLowerCase().equals(item.getLabel().toLowerCase())) {
					power.setLabel("Turn ON");
					state = !state;
					JOptionPane.showMessageDialog(null, "Backup is now OFF");

				} else if ("Turn ON".toLowerCase().equals(item.getLabel().toLowerCase())) {
					power.setLabel("Turn OFF");
					state = !state;
					JOptionPane.showMessageDialog(null, "Backup is now ON");
				}
			}
		};

		setHours.addActionListener(listener);
		setMinutes.addActionListener(listener);
		nextTest.addActionListener(listener);
		power.addActionListener(listener);

		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tray.remove(trayIcon);
				System.exit(0);
			}
		});
	}

	// Set tray icon
	protected static Image createImage(String description) {
		URL imageURL = null;
		try {
			imageURL = new URL("https://i.imgur.com/eLnExaV.png");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		if (imageURL == null) {
			return null;
		} else {
			return (new ImageIcon(imageURL, description)).getImage();
		}
	}

}
