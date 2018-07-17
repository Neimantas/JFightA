package models.constant;

public enum ItemType {

	ATTACK("Attack"), DEFENCE("Defence");

	private String _itemTypeTitle;

	private ItemType(String itemTypeTitle) {
		_itemTypeTitle = itemTypeTitle;
	}

	public static ItemType getByItemTypeTitle(String itemTypeTitle) {
		for (ItemType itemStatus : ItemType.values()) {
			if (itemStatus.getItemTypeTitle().equalsIgnoreCase(itemTypeTitle)) {
				return itemStatus;
			}
		}
		return null;
	}

	public String getItemTypeTitle() {
		return _itemTypeTitle;
	}

}
