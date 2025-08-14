use primes::{PrimeSet, Sieve};

pub fn sum_divisors(k: u32, shortcircuit: Option<u32>, divisors_bound: Option<u32>) -> u32 {
    divisors_with_primes(prime_factorization(k, shortcircuit), divisors_bound)
        .iter()
        .sum()
}

pub fn prime_factorization(k: u32, max_prime: Option<u32>) -> Vec<(u32, u32)> {
    let mut pset = Sieve::new();
    let mut pset_iter = pset.iter();
    let mut remainder = k;
    let mut result = Vec::new();
    while remainder > 1 {
        let mut pow = 0;
        let mut prime = pset_iter.next().unwrap() as u32;
        while remainder % prime != 0 {
            //If we are dealing with a k which is divided by a great prime, we are not interested
            //and becomes expensive to compute
            if let Some(max_prime) = max_prime {
                if prime > max_prime {
                    return Vec::new();
                }
            }
            prime = pset_iter.next().unwrap() as u32;
        }
        while remainder % prime == 0 {
            remainder /= prime;
            pow += 1;
        }
        result.push((prime, pow));
    }

    result
}

pub fn divisors_with_primes(
    prime_divisors: Vec<(u32, u32)>,
    divisors_bound: Option<u32>,
) -> Vec<u32> {
    let mut vec_iterator = prime_divisors.iter().map(|_| 0).collect::<Vec<u32>>();

    let iterator_top = prime_divisors
        .iter()
        .map(|(_, top)| *top)
        .collect::<Vec<u32>>();

    let primes = prime_divisors
        .iter()
        .map(|(primes, _)| *primes)
        .collect::<Vec<u32>>();

    let mut results = vec![1];

    while next(&mut vec_iterator, &iterator_top) {
        let divisor = primes
            .iter()
            .zip(&vec_iterator)
            .map(|(prime, power)| prime.pow(*power))
            .product();

        results.push(divisor);
    }

    //We just add up the divisors d which d > k / divisors_bound
    //For divisors_bound = 50, it means that elf d will still visit k
    if let Some(divisors_bound) = divisors_bound {
        let k: u32 = prime_divisors
            .iter()
            .map(|(prime, power)| prime.pow(*power))
            .product();
        results.retain(|d| *d >= k / divisors_bound);
    }

    results
}

pub fn next(vec_iterator: &mut [u32], iterator_top: &[u32]) -> bool {
    let mut i = 0;
    while i < vec_iterator.len() {
        if vec_iterator[i] == iterator_top[i] {
            vec_iterator[i] = 0;
        } else {
            vec_iterator[i] += 1;
            return true;
        }
        i += 1;
    }
    false
}
