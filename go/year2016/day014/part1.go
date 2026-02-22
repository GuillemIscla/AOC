package main

import (
	"crypto/md5"
	"encoding/hex"
	"strconv"
)

func part1() int {
	// v := 1
	// salt, _ := parseInput(&v)
	salt, _ := parseInput(nil)

	index := 0

	foundKeys := make(FoundKeys)
	fiveInARowRecord := make(FiveInARowRecord)

	for {
		if index >= 1000 {
			h := md5.Sum([]byte(salt + strconv.Itoa(index-1000)))
			hash := hex.EncodeToString(h[:])
			//We take all occurrences
			threeInARow := exploreHash(hash, 3)
			for _, t := range threeInARow {
				if fiveInARowRecord.Find(t, index-1000) {
					foundKeys.Add(index - 1000)
				}
				// If the first occurrence does not fly, we exit the loop all the same
				break
			}
		}
		h := md5.Sum([]byte(salt + strconv.Itoa(index)))
		hash := hex.EncodeToString(h[:])
		fiveInARow := exploreHash(hash, 5)

		for _, f := range fiveInARow {
			fiveInARowRecord.Add(f, index)
		}
		if len(foundKeys) >= 64 {
			break
		}
		index++

	}

	return index - 1000
}
