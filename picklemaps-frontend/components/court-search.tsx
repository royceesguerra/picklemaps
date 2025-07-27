'use client';

import { useState, useRef } from 'react';
import { Search, Star } from 'lucide-react';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { CourtSearchParams } from '@/domain/domain';

interface CourtSearchProps {
  searchCourts: (params: CourtSearchParams) => Promise<void>;
}

export default function CourtSearch(props: CourtSearchProps) {
  const { searchCourts } = props;

  const [query, setQuery] = useState('');
  const [minRating, setMinRating] = useState<number | undefined>(undefined);

  const isInitialMount = useRef(true);

  const handleSearch = async () => {
    await searchCourts({
      q: query,
      minRating,
    });
  };

  const handleSearchFormSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    await handleSearch();
  };

  const handleMinRatingFilter = async (value: number) => {
    if (minRating !== value) {
      setMinRating(value);

      setTimeout(() => {
        searchCourts({
          q: query,
          minRating: value,
        });
      }, 0);
    } else {
      setMinRating(undefined);

      setTimeout(() => {
        searchCourts({
          q: query,
          minRating: undefined,
        });
      }, 0);
    }
  };

  return (
    <div>
      <form onSubmit={handleSearchFormSubmit} className="flex gap-2 mb-4">
        <Input
          type="text"
          placeholder="Search for courts..."
          value={query}
          onChange={e => setQuery(e.target.value)}
          className="flex-grow"
        />
        <Button type="submit">
          <Search className="mr-2 h-4 w-4" /> Search
        </Button>
      </form>
      <div className="flex mb-8 gap-2">
        <Button
          onClick={() => handleMinRatingFilter(2)}
          variant={minRating === 2 ? 'default' : 'outline'}
        >
          <Star /> 2+
        </Button>

        <Button
          onClick={() => handleMinRatingFilter(3)}
          variant={minRating === 3 ? 'default' : 'outline'}
        >
          <Star /> 3+
        </Button>

        <Button
          onClick={() => handleMinRatingFilter(4)}
          variant={minRating === 4 ? 'default' : 'outline'}
        >
          <Star /> 4+
        </Button>
      </div>
    </div>
  );
}
