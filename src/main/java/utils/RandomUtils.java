package utils;

import java.util.Map;
import java.util.Set;
import java.util.SplittableRandom;

public class RandomUtils {

  private final SplittableRandom random = new SplittableRandom();

  public <K, V>  K getRandomKeyOfMap(Map<K,V> map) {
    return  getRandomValueOfSet(map.keySet());
  }

  public <K> K getRandomValueOfSet(Set<K> values) {
    int rnd = random.nextInt(0,values.size());
    int i = 0;
    for(K value : values) {
      if (i == rnd)
        return value;
      i++;
    }

    return null;
  }

  public boolean percent(int prob) {
    if(prob == 0) return false;
    int index = random.nextInt(0, 100);
    return prob >= index;
  }
}
