package com.onpu.domain.util


inline fun <T> T.onList() = listOf(this)
inline fun <T> T.onMutableList() = mutableListOf(this)

inline fun<T> MutableCollection<T>.removeIfAndReturn(crossinline predicate: (T) -> Boolean): MutableCollection<T> {
    val iterator = iterator()
    while (iterator.hasNext()) {
        val it = iterator.next()
        if (predicate(it)) iterator.remove()
    }
    return this
}

inline fun<T> MutableCollection<T>.removeIf(crossinline predicate: (T) -> Boolean): T? {
    val iterator = iterator()
    while (iterator.hasNext()) {
        val it = iterator.next()
        if (predicate(it)) return iterator.remove().run { it }
    }
    return null
}

inline fun<T> MutableCollection<T>.removeIfAndIndex(crossinline predicate: (T) -> Boolean): Int {
    val iterator = iterator()
    var index = 0
    while (iterator.hasNext()) {
        val it = iterator.next()
        if (predicate(it)) return  iterator.remove().run { index }
        index++
    }
    return -1
}

inline fun<T> MutableCollection<T>.deleteIf(crossinline predicate: (T) -> Boolean): Boolean {
    val iterator = iterator()
    while (iterator.hasNext()) {
        val it = iterator.next()
        if (predicate(it)) return iterator.remove().run { true }
    }
    return false
}

inline fun<T> MutableCollection<T>.clearIf(crossinline predicate: (T) -> Boolean): Boolean {
    var isClear = false
    val iterator = iterator()
    while (iterator.hasNext()) {
        val it = iterator.next()
        if (predicate(it)) iterator.remove().run { isClear = true }
    }
    return isClear
}

fun<T> MutableList<T>.getAndRemove(position: Int): T {
    val item = get(position)
    removeAt(position)
    return item
}

fun <T> MutableCollection<T>.add(item: T?) {
    item ?: return
    add(item)
}

inline fun<T> MutableCollection<T>.addIf(item: T, crossinline predicate: (T) -> Boolean) {
    val iterator = iterator()
    while (iterator.hasNext()) if (predicate(iterator.next())) add(item)
}

inline fun<T> MutableList<T>.setIf(item: T, crossinline predicate: (T) -> Boolean) {
    val index = indexIfOr(-1, predicate)
    if (!index.isMinusOne) set(index, item)
}

inline fun<T> MutableCollection<T>.addIfNotContains(item: T, crossinline predicate: (T) -> Boolean) {
    if (!containsIn(predicate)) add(item)
}

inline fun<T> MutableList<T>.addOrReplaceIf(item: T, crossinline predicate: (T) -> Boolean) {
    if (!containsIn(predicate)) add(item)
    else replaceIf(item, predicate)
}

inline fun<T> MutableList<T>.replaceIf(item: T?, crossinline predicate: (T) -> Boolean) {
    if (item == null) return
    indexIf(predicate)?.let { this[it] = item }
}

inline fun<T> MutableList<T>.replaceAndIndex(item: T?, crossinline predicate: (T) -> Boolean): Int? {
    if (item == null) return null
    return indexIf(predicate)?.also { this[it] = item }
}

inline fun<T> MutableList<T>.replaceWithCopyIf(
    crossinline predicate: (T) -> Boolean, crossinline copy: (T) -> T
) = indexIf(predicate)?.let { this[it] = copy(this[it]); it }

fun <T> MutableList<T>.replaceSpecified(position: Int, function: (T) -> T) {
    getOrNull(position)?.let { set(position, function(it)) }
}

inline fun<T> Collection<T>.indexIf(
    crossinline predicate: (T) -> Boolean, crossinline action: (position: Int) -> Unit) {
    val iterator = iterator()
    var isFound = false
    var index = -1
    while (iterator.hasNext()) { index++; if (predicate(iterator.next())) { isFound = true; break } }
    if (isFound) action.invoke(index)
}

inline fun<T> Collection<T>.indexIf(crossinline predicate: (T) -> Boolean): Int? {
    val iterator = iterator()
    var index = -1
    while (iterator.hasNext()) { index++; if (predicate(iterator.next())) return index }
    return null
}

inline fun<T> Collection<T>.indexIfMap(
    crossinline predicate: (T) -> Boolean, crossinline function: (index: Int, item: T) -> Unit) {
    var index = -1
    for (it in this) { index++; if (predicate(it)) return function(index, it) }
}

inline fun<T> MutableCollection<T>.indexIfAndRemove(
    crossinline predicate: (T) -> Boolean, crossinline action: (position: Int) -> Unit) {
    val iterator = iterator()
    var isFound = false
    var index = -1
    while (iterator.hasNext()) { index++; if (predicate(iterator.next())) { isFound = true; break } }
    if (isFound) { iterator.remove(); action.invoke(index) }
}

inline fun<T> Collection<T>.indexIfOr(default: Int = 0, crossinline predicate: (T) -> Boolean): Int {
    val iterator = iterator()
    var index = -1
    while (iterator.hasNext()) { index++; if (predicate(iterator.next())) return index }
    return default
}

inline fun<T> Collection<T>.containsIn(crossinline predicate: (T) -> Boolean): Boolean {
    for (it in this) if (predicate(it)) return true
    return false
}

inline fun <T, R> Iterable<T>?.mapMutable(crossinline transform: (T) -> R): MutableList<R> {
    if (this == null) return mutableListOf()
    return mapTo(mutableListOf(), transform)
}

inline fun <T> Iterable<T>?.mutFilter(crossinline predicate: (T) -> Boolean): MutableList<T> {
    if (this == null) return mutableListOf()
    return filterTo(mutableListOf(), predicate).toMutableList()
}

inline fun <T, R : Comparable<R>> Iterable<T>.sortedByMutable(crossinline selector: (T) -> R?): MutableList<T> {
    return sortedWith(compareBy(selector)).toMutableList()
}
fun <T> MutableList<T>?.orEmpty(): MutableList<T> = this ?: mutableListOf()

inline fun <T, R> Collection<T>.compareListsBy(
    items: Collection<R>,
    crossinline predicate: (first: T, second: R) -> Boolean,
    crossinline action: (first: T, second: R) -> Unit
) = forEach { first -> items.find { predicate(first, it) }?.let { action(first , it) } }

inline fun <T, R> MutableList<T>.compareByAndCopy(
    items: Collection<R>,
    crossinline predicate: (first: T, second: R) -> Boolean,
    crossinline copy: (first: T, second: R) -> T
) = forEachIndexed { index, first ->
    items.find { predicate(first, it) }?.let { this[index] = copy(first , it) }
}

inline fun<K, T, V> Map<K, T>.iterate(crossinline action: (K, T) -> V?) {
    for ((key, value) in entries) key?.let { k -> value?.let { action(k, it) } }
}
