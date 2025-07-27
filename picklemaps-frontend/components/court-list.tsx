'use client';

import CourtCard from '@/components/court-card';
import { CourtSummary } from '@/domain/domain';
import { Donut } from 'lucide-react';

interface CourtListProps {
  loading: boolean;
  courts: CourtSummary[];
}

export default function CourtList(props: CourtListProps) {
  const { loading, courts } = props;

  if (loading) {
    return <div>Loading courts...</div>;
  }

  if (courts.length == 0) {
    return (
      <div className="flex items-center justify-center min-h-[200px]">
        <div className="text-center">
          <div className="flex justify-center">
            <Donut size={100} className="opacity-[0.1]" />
          </div>

          <h3 className="mt-4 opacity-[0.3]">No Courts Available</h3>
        </div>
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
      {courts.map(court => (
        <CourtCard key={court.id} court={court} />
      ))}
    </div>
  );
}
