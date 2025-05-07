use primes::{self, PrimeSet, Sieve};

pub fn sum_divisors(k: u32, shortcircuit: Option<u32>) -> u32 {
    sum_divisors_with_prime_factorization(prime_factorization(k, shortcircuit))
}

fn sum_divisors_with_prime_factorization(prime_divisors: Vec<(u32, u32)>) -> u32 {
    let mut result = 1;
    for (prime, max_power) in prime_divisors {
        let mut factor = 0;
        let mut power = 0;
        while power <= max_power {
            factor += prime.pow(power);
            power += 1;
        }
        result *= factor;
    }
    result
}

pub fn prime_factorization(k: u32, shortcircuit: Option<u32>) -> Vec<(u32, u32)> {
    let mut pset = Sieve::new();
    let mut pset_iter = pset.iter();
    let mut remainder = k;
    let mut result = Vec::new();

    while remainder > 1 {
        let mut pow = 0;
        let mut prime = pset_iter.next().unwrap() as u32;
        while remainder % prime != 0 {
            if let Some(max_prime) = shortcircuit {
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

//If p is a big prime and 1 < k <= 50. Then the house k * p is much luckier than its neighbours since it gets
//presents from elves p and k * p and if d is divsor of k also elf (k/d) * p.
//Actually it gets as much presents as 11 * from sum_divisors(k) * p
pub fn find_lucky_house_with_k(k: u32, min_to_reach: u32) -> u32 {
    let sum_k_divisors = sum_divisors(k, None);
    //We start with a house (k * p) that with this extra luck will have presents equal or still less than min_to_reach
    let mut p = min_to_reach / (11 * sum_k_divisors);

    //We make the house have at least those presents
    if 11 * p * sum_k_divisors < min_to_reach {
        p += 1;
    }

    //We make sure p is prime
    while !primes::is_prime(p.into()) {
        p += 1;
    }

    k * p
}
