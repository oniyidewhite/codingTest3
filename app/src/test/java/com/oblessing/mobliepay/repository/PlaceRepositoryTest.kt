package com.oblessing.mobliepay.repository

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.oblessing.mobliepay.model.PageResult
import com.oblessing.mobliepay.model.SearchCriteria
import com.oblessing.mobliepay.network.PlaceEntityMapper
import com.oblessing.mobliepay.network.PlaceListEntity
import com.oblessing.mobliepay.network.PlaceWebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import kotlin.properties.Delegates

class PlaceRepositoryTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    private lateinit var webService: PlaceWebService
    private var limit by Delegates.notNull<Int>()
    private lateinit var mapper: PlaceEntityMapper
    private lateinit var repository: PlaceRepository

    @Before
    fun setUp() {
        limit = 1
        webService = mock()
        mapper = PlaceEntityMapper(limit)
        repository = PlaceRepository(webService, mapper, limit)
    }

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    @Test
    fun `place data should be null data if request failed`() = runBlockingTest {
        val entity: PlaceListEntity? = null

        whenever(webService.search("q", 0, 1)).thenReturn(entity)

        repository.findPlaces(SearchCriteria("q", 0)).collect(collector = object :
            FlowCollector<PageResult?> {
            override suspend fun emit(value: PageResult?) {
                assert(value == null)
            }
        })
    }

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    @Test
    fun `place data should data not be null if request successful`() = runBlockingTest {
        val entity: PlaceListEntity = PlaceListEntity(count = 1, offset = 0, places = emptyList())

        whenever(webService.search("q", 0, 1)).thenReturn(entity)

        repository.findPlaces(SearchCriteria("q", 0)).collect(collector = object :
            FlowCollector<PageResult?> {
            override suspend fun emit(value: PageResult?) {
                assert(value != null)
            }
        })
    }
}

@ExperimentalCoroutinesApi
class CoroutineTestRule(val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()) :
    TestWatcher() {
    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}