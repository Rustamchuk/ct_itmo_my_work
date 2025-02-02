using System;
using System.Collections.Generic;

class Trie
{
    private const int AlphSize = 30;
    private const int Root = 0;
    private List<Vertex> t;
    public List<int> nodeIndexes;

    public Trie(int n)
    {
        t = new List<Vertex>();
        nodeIndexes = new List<int>(new int[n]);
        Vertex v = new Vertex
        {
            SuffixLink = -1,
            Parent = -1,
            To = new int[AlphSize],
            Terminal = false
        };
        Array.Fill(v.To, -1);
        t.Add(v);
    }

    public void AddString(string s, int index)
    {
        int v = 0;
        foreach (char it in s)
        {
            int c = it - 'a';
            if (t[v].To[c] == -1)
            {
                Vertex u = new Vertex
                {
                    Parent = v,
                    SuffixLink = -1,
                    To = new int[AlphSize],
                    Terminal = false,
                    ParentChar = (char)c
                };
                Array.Fill(u.To, -1);
                t[v].To[c] = t.Count;
                t.Add(u);
            }
            v = t[v].To[c];
        }
        nodeIndexes[index] = v;
    }

    public void SetSuffixLinks()
    {
        Queue<int> q = new Queue<int>();
        q.Enqueue(0);

        while (q.Count > 0)
        {
            int v = q.Dequeue();
            SetSuffixLink(v, t[v].ParentChar);
            for (int c = 0; c < AlphSize; c++)
            {
                if (t[v].To[c] != -1)
                {
                    q.Enqueue(t[v].To[c]);
                }
            }
        }
    }

    public void MarkTerminals()
    {
        Dfs(Root);
    }

    private bool Dfs(int v)
    {
        bool terminal = t[v].Terminal;
        foreach (int to in t[v].RSuffixLinks)
        {
            terminal |= Dfs(to);
        }
        t[v].Terminal = terminal;
        return t[v].Terminal;
    }

    public int Next(int v, char c)
    {
        while (v != -1 && t[v].To[c] == -1)
        {
            v = t[v].SuffixLink;
        }
        if (v != -1)
        {
            v = t[v].To[c];
        }
        return v == -1 ? Root : v;
    }

    public bool IsTerminal(int v)
    {
        return t[v].Terminal;
    }

    public void ProcessText(string text)
    {
        int v = 0;
        t[v].Terminal = true;
        foreach (char it in text)
        {
            int c = it - 'a';
            v = Next(v, (char)c);
            t[v].Terminal = true;
        }
    }

    private void SetSuffixLink(int v, char c)
    {
        if (v == Root)
        {
            t[v].SuffixLink = -1;
            return;
        }

        int p = t[v].Parent;
        p = t[p].SuffixLink;

        while (p != -1 && t[p].To[c] == -1)
        {
            p = t[p].SuffixLink;
        }

        if (p == -1)
        {
            t[v].SuffixLink = Root;
            t[Root].RSuffixLinks.Add(v);
        }
        else
        {
            int u = t[p].To[c];
            t[v].SuffixLink = u;
            t[u].RSuffixLinks.Add(v);
        }
    }

    private class Vertex
    {
        public int[] To;
        public List<int> RSuffixLinks = new List<int>();
        public bool Terminal;
        public int Parent;
        public int SuffixLink;
        public char ParentChar;
    }
}

class Program
{
    static void Main()
    {
        int n = int.Parse(Console.ReadLine());
        Trie trie = new Trie(n);

        for (int i = 0; i < n; i++)
        {
            string s = Console.ReadLine();
            trie.AddString(s, i);
        }

        string text = Console.ReadLine();

        trie.SetSuffixLinks();
        trie.ProcessText(text);
        trie.MarkTerminals();

        for (int i = 0; i < n; i++)
        {
            if (trie.IsTerminal(trie.nodeIndexes[i]))
            {
                Console.WriteLine("YES");
            }
            else
            {
                Console.WriteLine("NO");
            }
        }
    }
}