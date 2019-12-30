package hook;

import gui.MainWindow;
import javafx.application.Platform;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import lc.kra.system.keyboard.event.GlobalKeyListener;
import lc.kra.system.mouse.GlobalMouseHook;
import lc.kra.system.mouse.event.GlobalMouseAdapter;
import lc.kra.system.mouse.event.GlobalMouseEvent;

import java.awt.AWTException;
import java.awt.Robot;

/**
 * Hook Controller
 * catches key presses and triggers key related functions
 *
 */

public class HookController {

    private MainWindow gui;
    private GlobalKeyboardHook hook;
    private GlobalMouseHook mouseHook;
    private Robot rot;
    private boolean isCtrlOn;

    public HookController() {
        init();
    }

    public void init() {
		try {
			rot = new Robot();
		} catch (AWTException e) {
			// Teleport penguins
		}

        hook = new GlobalKeyboardHook();

        hook.addKeyListener(new GlobalKeyListener() {

            @Override
            public void keyReleased(GlobalKeyEvent event) {
            	System.out.println("#" + event.getVirtualKeyCode() + "#");
            	if(event.getVirtualKeyCode() == GlobalKeyEvent.VK_LCONTROL) {
            		isCtrlOn = false;
            	}
                if(gui != null) {

                	// CTRL + i
                    if(event.isControlPressed() && (event.getVirtualKeyCode() == 222)) {
                        Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                                gui.speakQueryUsClicked();
                            }
                        });
                    }
                    // CTRL + ş
                    else if(event.isControlPressed() && (event.getVirtualKeyCode() == 186)) {
                        Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                                gui.speakQueryUkClicked();
                            }
                        });
                    }
                    // CTRL + ç
                    else if (event.isControlPressed() && (event.getVirtualKeyCode() == 220)) {

                    	if(rot != null) {
	            			rot.keyPress(java.awt.event.KeyEvent.VK_CONTROL);
	            			rot.keyPress(java.awt.event.KeyEvent.VK_C);
	            			rot.keyRelease(java.awt.event.KeyEvent.VK_CONTROL);
	            			rot.keyRelease(java.awt.event.KeyEvent.VK_C);
                    	}

                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {
                                gui.translateClicked();

                            }
                        });
                    }
                    // CTRL + ü
                    else if (event.isControlPressed() && (event.getVirtualKeyCode() == 221)) {
                        hook.shutdownHook();
                        mouseHook.shutdownHook();
                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {
                                gui.createLastPopup();

                            }
                        });
                    }
                }
            }

            @Override
            public void keyPressed(GlobalKeyEvent event) {
            	if(event.getVirtualKeyCode() == GlobalKeyEvent.VK_LCONTROL) {
            		isCtrlOn = true;
            	}
            }
        });

        mouseHook = new GlobalMouseHook();
        mouseHook.addMouseListener(new GlobalMouseAdapter() {
        	@Override
        	public void mouseMoved(GlobalMouseEvent event) {
        		gui.setCursorPos(event.getX(), event.getY());
        	}

        	@Override
        	public void mousePressed(GlobalMouseEvent event) {
        		if(isCtrlOn) {
        			if(event.getButton() == GlobalMouseEvent.BUTTON_MIDDLE) {

        				if(rot != null) {
        					rot.keyPress(java.awt.event.KeyEvent.VK_CONTROL);
        					rot.keyPress(java.awt.event.KeyEvent.VK_C);
        					rot.keyRelease(java.awt.event.KeyEvent.VK_CONTROL);
        					rot.keyRelease(java.awt.event.KeyEvent.VK_C);
        				}

        				Platform.runLater(new Runnable() {

        					@Override
        					public void run() {
        						gui.translateClicked();

        					}
        				});
        			}
        		}
        	}
        });

    }

    public void setGui(MainWindow gui) {
        this.gui = gui;
    }
}
