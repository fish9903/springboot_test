package hello.itemservice.domain.item;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ItemRepositoryTest {

    ItemRepository itemRepository = new ItemRepository();

    @AfterEach
    void afterEach() {
        itemRepository.clearStore();
    }

    @Test
    void save() {
        Item item = new Item("itemA", 10000, 10);

        Item savedItem = itemRepository.save(item);
        Item findItem = itemRepository.findById(savedItem.getId());

        //test
        assertThat(findItem).isEqualTo(savedItem);
    }

    @Test
    void findAll() {
        Item item1 = new Item("itemA", 10000,10);
        Item item2 = new Item("itemA", 10000,10);
        Item item3 = new Item("itemA", 10000,10);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        List<Item> result = itemRepository.findAll();

        //test
        System.out.println(result);
        assertThat(result.size()).isEqualTo(3);
        assertThat(result).contains(item1, item2, item3);
    }

    @Test
    void updateItem() {
        Item item1 = new Item("itemA", 10000,10);

        Item savedItem = itemRepository.save(item1);
        Long itemId = savedItem.getId();

        Item updateItem = new Item("item2", 20000, 30);
        itemRepository.update(itemId, updateItem);

        Item findItem = itemRepository.findById(itemId);

        //test
        assertThat(findItem.getItemName()).isEqualTo(updateItem.getItemName());
        assertThat(findItem.getPrice()).isEqualTo(updateItem.getPrice());
        assertThat(findItem.getQuantity()).isEqualTo(updateItem.getQuantity());
    }
}