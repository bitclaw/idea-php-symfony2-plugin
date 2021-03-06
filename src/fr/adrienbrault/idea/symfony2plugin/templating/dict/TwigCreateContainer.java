package fr.adrienbrault.idea.symfony2plugin.templating.dict;

import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TwigCreateContainer {

    private Map<String, Integer> extendHap = new HashMap<String, Integer>();
    private Map<String, Integer> blockHap = new HashMap<String, Integer>();

    public void addExtend(String extend) {
        if(extendHap.containsKey(extend)) {
            extendHap.put(extend, extendHap.get(extend) + 1);
        } else {
            extendHap.put(extend, 1);
        }
    }

    public void addBlock(String block) {
        if(blockHap.containsKey(block)) {
            blockHap.put(block, blockHap.get(block) + 1);
        } else {
            blockHap.put(block, 1);
        }
    }

    @Nullable
    public String getExtend() {
        Map<String, Integer> extendsMap = this.getExtends();
        return extendsMap.size() > 0 ? extendsMap.keySet().iterator().next() : null;
    }

    public Map<String, Integer> getExtends() {
        return sortByValue(extendHap);
    }

    public Map<String, Integer> getBlocks() {
        return sortByValue(blockHap);
    }

    public Collection<String> getBlockNames(int limit) {

        List<String> strings = new ArrayList<String>(getBlocks().keySet());
        if(strings.size() > limit) {
            strings = strings.subList(0, limit);
        }

        return strings;
    }

    private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map )
    {
        List<Map.Entry<K, V>> list =  new LinkedList<Map.Entry<K, V>>( map.entrySet() );
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        Collections.reverse(list);

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put( entry.getKey(), entry.getValue() );
        }

        return result;
    }

}
