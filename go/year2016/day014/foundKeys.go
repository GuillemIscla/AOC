package main

type FoundKeys map[int]struct{}

func (ck FoundKeys) Add(i int) {
	ck[i] = struct{}{}
}
