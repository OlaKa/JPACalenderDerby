package com.mastertheboss.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Item {
	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@ManyToOne
	private ShoppingList shoppingList;

	public Item() {}

	public Item(String name, ShoppingList shoppingList) {
		this.name = name;
		this.shoppingList = shoppingList;
	}

	public Item(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ShoppingList getShoppingList() {
		return shoppingList;
	}

	public void setShoppingList(ShoppingList shoppingList) {
		this.shoppingList = shoppingList;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", shopping list="
				+ shoppingList.getName() + "]";
	}

}
