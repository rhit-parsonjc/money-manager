package com.moneymanager.api.repositories;

import com.moneymanager.api.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
