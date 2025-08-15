package main

import (
	"bufio"
	"os"
)

func parseInput(value *int) ([]IPv7Address, error) {
	if value == nil {
		filename := "../../inputs/year2016/day007.txt"
		file, err := os.Open(filename)
		if err != nil {
			return []IPv7Address{}, err
		}
		defer file.Close()
		scanner := bufio.NewScanner(file)
		array := make([]IPv7Address, 0)
		for scanner.Scan() {
			array = append(array, NewIPv7Address(scanner.Text()))
		}
		return array, nil
	} else if *value == 1 {
		return []IPv7Address{
			NewIPv7Address("abba[mnop]qrst"),
			NewIPv7Address("abcd[bddb]xyyx"),
			NewIPv7Address("aaaa[qwer]tyui"),
			NewIPv7Address("ioxxoj[asdfgh]zxcvbn"),
		}, nil
	} else if *value == 2 {
		return []IPv7Address{
			NewIPv7Address("aba[bab]xyz"),
			NewIPv7Address("xyx[xyx]xyx"),
			NewIPv7Address("aaa[kek]eke"),
			NewIPv7Address("zazbz[bzb]cdb"),
		}, nil
	}
	return []IPv7Address{}, nil
}
