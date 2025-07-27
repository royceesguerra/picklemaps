'use client';

import CourtSearch from '@/components/court-search';
import CourtList from '@/components/court-list';
import Hero from '@/components/hero';
import { useEffect, useState } from 'react';
import { CourtSearchParams, CourtSummary } from '@/domain/domain';
import { useAppContext } from '@/providers/app-context-provider';
import {
  Pagination,
  PaginationContent,
  PaginationEllipsis,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from '@/components/ui/pagination';

export default function Home() {
  const { apiService } = useAppContext();

  const [loading, setLoading] = useState(true);
  const [courts, setCourts] = useState<CourtSummary[]>([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState<number | undefined>(undefined);
  const [first, setFirst] = useState<boolean>(true);
  const [last, setLast] = useState<boolean>(false);
  const [currentSearchParams, setCurrentSearchParams] = useState<CourtSearchParams>({});

  const searchCourts = async (params: CourtSearchParams, targetPage?: number) => {
    try {
      setLoading(true);
      setCurrentSearchParams(params);

      const paginatedParams = {
        ...params,
        page: targetPage || page,
        size: 8,
      };

      console.log(`Searching for Courts: ${JSON.stringify(params)}`);
      if (!apiService?.searchCourts) {
        throw 'ApiService is not initialized!';
        return;
      }
      const courts = await apiService.searchCourts(paginatedParams);

      if (courts) {
        setTotalPages(courts.totalPages);
        setFirst(courts.first);
        setLast(courts.last);
        setCourts(courts.content);
      }
    } catch (error) {
      console.error('Error fetching courts:', error);
    } finally {
      setLoading(false);
    }
  };

  const handlePageChange = async (newPage: number) => {
    setPage(newPage);
    await searchCourts(currentSearchParams, newPage);
  };

  const getPageNumbers = () => {
    if (!totalPages) return [];

    const pageNumbers: (number | 'ellipsis')[] = [];
    const maxVisiblePages = 5;

    if (totalPages <= maxVisiblePages) {
      return Array.from({ length: totalPages }, (_, i) => i + 1);
    }

    pageNumbers.push(1);

    if (page > 3) {
      pageNumbers.push('ellipsis');
    }

    for (let i = Math.max(2, page - 1); i <= Math.min(totalPages - 1, page + 1); i++) {
      pageNumbers.push(i);
    }

    if (page < totalPages - 2) {
      pageNumbers.push('ellipsis');
    }

    if (totalPages > 1) {
      pageNumbers.push(totalPages);
    }

    return pageNumbers;
  };

  useEffect(() => {
    const doUseEffect = async () => {
      await searchCourts({});
    };

    if (!apiService) {
      return;
    }

    doUseEffect();
  }, [apiService]);

  return (
    <div>
      <Hero />
      <main className="max-w-[1200px] mx-auto px-4 py-8">
        <CourtSearch searchCourts={searchCourts} />
        <CourtList loading={loading} courts={courts} />
        {totalPages && totalPages > 1 && (
          <div className="mt-8">
            <Pagination>
              <PaginationContent>
                {!first && (
                  <PaginationItem>
                    <PaginationPrevious
                      href="#"
                      onClick={e => {
                        e.preventDefault();
                        handlePageChange(page - 1);
                      }}
                    />
                  </PaginationItem>
                )}

                {getPageNumbers().map((pageNum, index) => (
                  <PaginationItem key={`${pageNum}-${index}`}>
                    {pageNum === 'ellipsis' ? (
                      <PaginationEllipsis />
                    ) : (
                      <PaginationLink
                        href="#"
                        isActive={pageNum === page}
                        onClick={e => {
                          e.preventDefault();
                          handlePageChange(pageNum);
                        }}
                      >
                        {pageNum}
                      </PaginationLink>
                    )}
                  </PaginationItem>
                ))}

                {!last && (
                  <PaginationItem>
                    <PaginationNext
                      href="#"
                      onClick={e => {
                        e.preventDefault();
                        handlePageChange(page + 1);
                      }}
                    />
                  </PaginationItem>
                )}
              </PaginationContent>
            </Pagination>
          </div>
        )}
      </main>
    </div>
  );
}
