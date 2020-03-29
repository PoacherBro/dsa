package binarysearch

// Search will return index of matched `key` in `src` array
func Search(key int, src []int) int {
	low, high := 0, len(src)-1
	for low <= high {
		mid := low + (high-low)/2
		switch {
		case key < src[mid]:
			high = mid - 1
		case key > src[mid]:
			low = mid + 1
		default:
			return mid
		}
	}
	return -1
}
