package solutions.vjk.catalist.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import solutions.vjk.catalist.interfaces.*
import solutions.vjk.catalist.repositories.*
import solutions.vjk.catalist.repositories.dao.*
import solutions.vjk.catalist.viewmodels.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CatalistModule {

    @Singleton
    @Provides
    fun catalistDbFactory(@ApplicationContext context: Context): CatalistDb {
        return Room
            .databaseBuilder(
                context,
                CatalistDb::class.java,
                "catalist_database"
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun assigneeDaoFactory(database: CatalistDb): AssigneeDao {
        return database.assigneeDao()
    }

    @Singleton
    @Provides
    fun categoryDaoFactory(database: CatalistDb): CategoryDao {
        return database.categoryDao()
    }

    @Singleton
    @Provides
    fun itemDaoFactory(database: CatalistDb): ItemDao {
        return database.itemDao()
    }

    @Singleton
    @Provides
    fun toDoListDaoFactory(database: CatalistDb): ToDoListDao {
        return database.toDoListDao()
    }

    @Singleton
    @Provides
    fun settingsDaoFactory(database: CatalistDb): SettingsDao {
        return database.settingsDao()
    }

    @Singleton
    @Provides
    fun assigneeRepositoryFactory(dao: AssigneeDao): IAssigneeRepository {
        return AssigneeRepository(dao)
    }

    @Singleton
    @Provides
    fun categoryRepositoryFactory(dao: CategoryDao): ICategoryRepository {
        return CategoryRepository(dao)
    }

    @Singleton
    @Provides
    fun itemRepositoryFactory(
        itemDao: ItemDao,
        assigneeDao: AssigneeDao,
        settingsDao: SettingsDao
    ): IItemRepository {
        return ItemRepository(itemDao, assigneeDao, settingsDao)
    }

    @Singleton
    @Provides
    fun settingsRepositoryFactory(dao: SettingsDao): ISettingsRepository {
        return SettingsRepository(dao)
    }

    @Singleton
    @Provides
    fun listRepositoryFactory(dao: ToDoListDao): IListRepository {
        return ToDoListRepository(dao)
    }

    @Provides
    fun settingsViewModelFactory(settingsRepository: ISettingsRepository): SettingsViewModel {
        return SettingsViewModel.create(settingsRepository)
    }

    @Provides
    fun editItemViewModelFactory(
        assigneeRepository: IAssigneeRepository,
        categoryRepository: ICategoryRepository,
        itemRepository: IItemRepository
    ): EditItemViewModel {
        return EditItemViewModel.create(assigneeRepository, categoryRepository, itemRepository)
    }

    @Provides
    fun newItemViewModelFactory(
        assigneeRepository: IAssigneeRepository,
        categoryRepository: ICategoryRepository,
        itemRepository: IItemRepository
    ): NewItemViewModel {
        return NewItemViewModel.create(assigneeRepository, categoryRepository, itemRepository)
    }

    @Provides
    fun manageAssigneesViewModelFactory(
        assigneeRepository: IAssigneeRepository,
        itemRepository: IItemRepository
    ): ManageAssigneesViewModel {
        return ManageAssigneesViewModel.create(assigneeRepository, itemRepository)
    }

    @Provides
    fun newListViewModelFactory(
        listRepository: IListRepository,
        categoryRepository: ICategoryRepository,
        application: Application
    ): NewListViewModel {
        return NewListViewModel.create(listRepository, categoryRepository, application)
    }

    @Provides
    fun openListViewModelFactory(
        listRepository: IListRepository,
        application: Application
    ): OpenListViewModel {
        return OpenListViewModel.create(listRepository, application)
    }
}