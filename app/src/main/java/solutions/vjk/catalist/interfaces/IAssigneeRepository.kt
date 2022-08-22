package solutions.vjk.catalist.interfaces

import solutions.vjk.catalist.models.Assignee

interface IAssigneeRepository : IRepository<Assignee> {
    suspend fun read(name: String): Assignee?
}