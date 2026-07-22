package com.gladtek.vaadin.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;

import java.util.List;

public class GridPaginator extends HorizontalLayout {

    private final Button previousButton;
    private final Button nextButton;
    private final Span pageLabel;
    private final Select<Integer> pageSizeSelect;

    private int page = 0;
    private int pageSize;
    private int itemCount = 0;
    private boolean rtl = false;
    private Runnable onPageChanged = () -> {
    };

    public GridPaginator(int defaultPageSize, List<Integer> pageSizeOptions) {
        this.pageSize = defaultPageSize;

        previousButton = new Button(VaadinIcon.ANGLE_LEFT.create());
        previousButton.addClickListener(e -> {
            if (page > 0) {
                page--;
                updateLabel();
                onPageChanged.run();
            }
        });

        nextButton = new Button(VaadinIcon.ANGLE_RIGHT.create());
        nextButton.addClickListener(e -> {
            if (page < getPageCount() - 1) {
                page++;
                updateLabel();
                onPageChanged.run();
            }
        });

        pageLabel = new Span();
        // Page counter is always "current / total" left-to-right, regardless of UI language
        pageLabel.getStyle().set("direction", "ltr").set("unicode-bidi", "isolate");

        pageSizeSelect = new Select<>();
        pageSizeSelect.setItems(pageSizeOptions);
        pageSizeSelect.setValue(defaultPageSize);
        pageSizeSelect.setWidth("85px");
        pageSizeSelect.addValueChangeListener(e -> {
            Integer value = e.getValue();
            if (value != null && value > 0) {
                pageSize = value;
                page = 0;
                updateLabel();
                onPageChanged.run();
            }
        });

        setAlignItems(FlexComponent.Alignment.CENTER);
        setSpacing(true);
        setPadding(false);
        getStyle().set("margin-top", "1rem");
        add(previousButton, pageLabel, nextButton, pageSizeSelect);

        updateLabel();
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
        int maxPage = Math.max(0, getPageCount() - 1);
        if (page > maxPage) {
            page = maxPage;
        }
        updateLabel();
    }

    public void setPage(int page) {
        this.page = Math.max(0, page);
        updateLabel();
    }

    public void reset() {
        setPage(0);
    }

    public int getOffset() {
        return page * pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageCount() {
        return Math.max(1, (int) Math.ceil(itemCount / (double) pageSize));
    }

    public void onPageChanged(Runnable listener) {
        this.onPageChanged = listener;
    }

    /**
     * Swaps the previous/next arrow icons so they still point toward the visual
     * "backward"/"forward" direction once the surrounding layout is mirrored for RTL.
     */
    public void setRtl(boolean rtl) {
        if (this.rtl == rtl) {
            return;
        }
        this.rtl = rtl;
        previousButton.setIcon(rtl ? VaadinIcon.ANGLE_RIGHT.create() : VaadinIcon.ANGLE_LEFT.create());
        nextButton.setIcon(rtl ? VaadinIcon.ANGLE_LEFT.create() : VaadinIcon.ANGLE_RIGHT.create());
    }

    private void updateLabel() {
        int pageCount = getPageCount();
        pageLabel.setText((page + 1) + " / " + pageCount);
        previousButton.setEnabled(page > 0);
        nextButton.setEnabled(page < pageCount - 1);
    }
}
