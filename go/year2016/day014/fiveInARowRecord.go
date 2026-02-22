package main

type FiveInARowRecord map[rune][]int

func (frr FiveInARowRecord) Cleanup(r rune, index int) {
	i := 0
	for i = 0; i < len(frr[r]); i++ {
		if frr[r][i] > index {
			break
		}
	}

	frr[r] = frr[r][i:]
}

func (frr FiveInARowRecord) Add(r rune, index int) {
	frr[r] = append(frr[r], index)
}

func (frr FiveInARowRecord) Find(r rune, index int) bool {
	frr.Cleanup(r, index)

	return len(frr[r]) > 0
}

func exploreHash(hash string, length int) []rune {
	i := 0
	result := []rune{}
	hashInRunes := []rune(hash)

	for i < len(hashInRunes) {

		j := i + 1
		for j < len(hashInRunes) {
			if hashInRunes[j] != hashInRunes[i] {
				break
			}
			j++
		}
		if j-i+1 > length {
			result = append(result, hashInRunes[i])
		}
		i = j
	}

	return result
}
