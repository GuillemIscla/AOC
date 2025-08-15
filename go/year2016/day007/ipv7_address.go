package main

import (
	"strings"
)

type IPv7Address struct {
	outside  []string
	hypernet []string
}

func NewIPv7Address(raw string) IPv7Address {
	var parts []string

	parts = strings.FieldsFunc(raw, func(r rune) bool {
		return r == '[' || r == ']'
	})

	ip := IPv7Address{}

	for i, part := range parts {
		if i%2 == 0 {
			ip.outside = append(ip.outside, part)
		} else {
			ip.hypernet = append(ip.hypernet, part)
		}
	}
	return ip
}

func (ip IPv7Address) SupportsTLS() bool {
	for _, hypernet := range ip.hypernet {
		if HasABBA(hypernet) {
			return false
		}
	}
	for _, outside := range ip.outside {
		if HasABBA(outside) {
			return true
		}
	}
	return false
}

func HasABBA(part string) bool {
	for i, _ := range part {
		if i >= 3 && part[i-3] == part[i] && part[i-2] == part[i-1] && part[i] != part[i-2] {
			return true
		}
	}
	return false
}

func (ip IPv7Address) SupportsSSL() bool {
	var hypernetBAB []string
	for _, hypernet := range ip.hypernet {
		hypernetBAB = append(hypernetBAB, GetABASequences(hypernet)...)
	}

	var outsideABA []string
	for _, outside := range ip.outside {
		outsideABA = append(outsideABA, GetABASequences(outside)...)
	}

	for _, bab := range hypernetBAB {
		for _, aba := range outsideABA {
			if bab[0] == aba[1] && bab[1] == aba[0] {
				return true
			}
		}
	}
	return false
}

func GetABASequences(raw string) []string {
	var hypernetBAB []string

	for i := 0; i < len(raw)-2; i++ {
		if raw[i] == raw[i+2] && raw[i] != raw[i+1] {
			hypernetBAB = append(hypernetBAB, raw[i:i+3])
		}
	}
	return hypernetBAB
}
