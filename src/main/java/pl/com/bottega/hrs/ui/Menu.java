package pl.com.bottega.hrs.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private List<MenuItem> items = new ArrayList<>();
    private String lastItemLabel;
    private Scanner scanner;
    private String titleLabel;

    public Menu (Scanner scanner) {
        this.scanner = scanner;
    }

    public void show() {
        while(true) {

            showTitle();
            showItems();
            showLastItem();
            int decision = getUserDecision();
            if (wantsToQuit(decision))
                return;

            processAction(decision);
        }

    }

    private void processAction(int decision) {
        if (decision >=1 && decision <= items.size()+1) {
            MenuItem item = items.get(decision -1);
            item.executeAction();
        }
        else
            System.out.println("Sorry nie rozumiem");

    }

    private boolean wantsToQuit(int decision) {
        return decision == items.size()+1;
    }

    private int getUserDecision() {
        System.out.print("Co chcesz zrobic?");
        String userInput = scanner.nextLine();
        try {
            return Integer.parseInt(userInput);
        }
        catch (NumberFormatException nfe) {
            return -1;
        }
    }

    private void showLastItem() {
        System.out.println(String.format(" %d. %s", items.size() +1, lastItemLabel));

    }

    private void showItems() {
        for (MenuItem item : items)
            item.show();
    }

    public void setLastItemLabel(String label) {
        lastItemLabel = label;

    }

    public void addItem(String label, Runnable action) {

        items.add(new MenuItem(items.size()+1, label, action));

    }

    private void showTitle() {
        if(titleLabel != null)
            System.out.println(titleLabel);
    }

    public void setTitleLabel(String titleLabel) {
        this.titleLabel = titleLabel;
    }
}
