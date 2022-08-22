package solutions.vjk.catalist.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import solutions.vjk.catalist.interfaces.ICategoryRepository
import solutions.vjk.catalist.interfaces.IListRepository
import solutions.vjk.catalist.models.Category
import solutions.vjk.catalist.models.ToDoList
import javax.inject.Inject

class Initializer @Inject constructor(
    private val listRepository: IListRepository,
    private val categoryRepository: ICategoryRepository
) {
    suspend fun initialize(
        list: ToDoList,
        categories: List<Category>,
    ) {
        withContext(Dispatchers.IO) {
            listRepository.insert(list)
            val insertedList = listRepository.read(list.name)
            if (insertedList != null) {
                for (category in categories) {
                    val newCategory = category.copy(listId = insertedList.id)
                    categoryRepository.insert(newCategory)
                }
            }
        }
    }
}