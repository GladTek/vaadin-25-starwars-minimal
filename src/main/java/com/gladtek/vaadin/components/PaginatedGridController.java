package com.gladtek.vaadin.components;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PaginatedGridController<T> {

    private final Grid<T> grid;
    private final GridPaginator paginator;
    private final List<T> allItems;
    private Predicate<T> filter = item -> true;

    public PaginatedGridController(Grid<T> grid, GridPaginator paginator, List<T> allItems) {
        this.grid = grid;
        this.paginator = paginator;
        this.allItems = allItems;

        grid.setAllRowsVisible(true);
        paginator.onPageChanged(this::refresh);
        grid.addSortListener(event -> {
            paginator.reset();
            refresh();
        });

        // Populate immediately so the grid has data before beforeEnter (which may
        // select an item) runs. Refreshing again later would call grid.setItems()
        // again, which creates a new DataProvider and clears any selection.
        refresh();
    }

    public void setFilter(Predicate<T> filter) {
        this.filter = filter;
        refresh();
    }

    public void refresh() {
        List<T> filtered = filteredAndSorted();
        paginator.setItemCount(filtered.size());
        List<T> page = filtered.stream()
                .skip(paginator.getOffset())
                .limit(paginator.getPageSize())
                .collect(Collectors.toList());
        grid.setItems(page);
    }

    public void selectItem(T item) {
        List<T> filtered = filteredAndSorted();
        int index = filtered.indexOf(item);
        if (index < 0) {
            return;
        }
        paginator.setItemCount(filtered.size());
        paginator.setPage(index / paginator.getPageSize());
        refresh();
        grid.select(item);
    }

    private List<T> filteredAndSorted() {
        List<T> filtered = allItems.stream().filter(filter).collect(Collectors.toList());
        Comparator<T> comparator = buildComparator();
        if (comparator != null) {
            filtered.sort(comparator);
        }
        return filtered;
    }

    private Comparator<T> buildComparator() {
        Comparator<T> comparator = null;
        for (GridSortOrder<T> order : grid.getSortOrder()) {
            Comparator<T> columnComparator = order.getSorted().getComparator(order.getDirection());
            comparator = comparator == null ? columnComparator : comparator.thenComparing(columnComparator);
        }
        return comparator;
    }
}
