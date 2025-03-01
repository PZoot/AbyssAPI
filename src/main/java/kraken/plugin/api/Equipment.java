package kraken.plugin.api;

import abyss.plugin.api.extensions.Extension;
import abyss.plugin.api.extensions.SimpleExtensionContainer;
import abyss.plugin.api.variables.VariableManager;
import abyss.plugin.api.widgets.EquipmentWidgetExtension;

import java.util.LinkedList;
import java.util.List;

/**
 * Provides simplified access to the equipment widget.
 */
public final class Equipment extends SimpleExtensionContainer {

    public static final Equipment EQUIPMENT = new Equipment();

    private Equipment() {
        setExtension(new EquipmentWidgetExtension(670, 94, 1464, 15));
    }

    /**
     * Retrieves all items displayed in the equipment widget.
     *
     * @return All items displayed in the equipment widget.
     */
    public static WidgetItem[] getItems() {
        return getItems(false);
    }

    public static WidgetItem[] getItems(boolean showCosmetic) {
        if(!EQUIPMENT.hasExtension(EquipmentWidgetExtension.class)) {
            return new WidgetItem[0];
        }

        EquipmentWidgetExtension ext = (EquipmentWidgetExtension)EQUIPMENT.getExt(EquipmentWidgetExtension.class);
        int containerId = ext.getEquipmentContainerId();
        if(showCosmetic) {
            containerId = ext.getCosmeticContainerId();
        }

        ItemContainer container = ItemContainers.byId(containerId);
        if (container == null) {
            return new WidgetItem[0];
        }

        List<WidgetItem> list = new LinkedList<>();
        Item[] containerItems = container.getItems();
        for (int i = 0; i < containerItems.length; i++) {
            Item item = containerItems[i];
            if (item.getId() != -1) {
                WidgetItem wItem = new WidgetItem(item.getId(), item.getAmount(), i, Widgets.hash(ext.getRootId(), ext.getChildId()), container);

                Extension itemExt = VariableManager.getExt(item.getId());
                if(itemExt != null) {
                    wItem.setExtension(itemExt);
                }

                list.add(wItem);
            }
        }
        return list.toArray(new WidgetItem[0]);
    }


    /**
     * Finds the first item that passes the provided filter.
     *
     * @param filter The filter that items must pass through in order to be accepted.
     * @return The first item that passed the filter.
     */
    public static WidgetItem first(Filter<WidgetItem> filter) {
        for (WidgetItem item : getItems()) {
            if (filter.accept(item)) {
                return item;
            }
        }

        return null;
    }
}
