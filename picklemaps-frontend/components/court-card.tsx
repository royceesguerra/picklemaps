import Image from 'next/image';
import Link from 'next/link';
import { Star, Heart, Share2 } from 'lucide-react';
import { Card } from '@/components/ui/card';
import { CourtSummary } from '@/domain/domain';

interface CourtCardProps {
  court: CourtSummary;
}

const getImage = (court: CourtSummary) => {
  if (court.photos && null != court.photos[0] && null != court.photos[0].url) {
    return `/api/photos/${court.photos[0].url}`;
  } else {
    return null;
  }
};

export default function CourtCard({ court }: CourtCardProps) {
  return (
    <Link href={`/courts/${court.id}`}>
      <Card className="overflow-hidden hover:shadow-lg transition-shadow">
        <div className="p-4">
          <div className="flex items-center gap-3 mb-4">
            {/* <Avatar>
              <AvatarImage src={court.latestReview?.user.avatar} />
              <AvatarFallback>{court.latestReview?.user.name[0]}</AvatarFallback>
            </Avatar> */}
            <div>
              <p className="font-medium">{court.name}</p>
              <p className="text-sm text-muted-foreground">
                {court.address.city}, {court.address.province}
              </p>
            </div>
          </div>
          <div className="aspect-[4/3] relative mb-4">
            <Image
              src={getImage(court) || '/placeholder.svg?height=300&width=400'}
              alt={court.name}
              fill
              className="object-cover rounded-md"
            />
          </div>
          <h2 className="text-xl font-semibold mb-2">{court.name}</h2>
          <div className="flex items-center gap-1 mb-2">
            {Array.from({ length: 5 }).map((_, i) => (
              <Star
                key={i}
                className={`w-4 h-4 ${
                  court.averageRating && i < Math.round(court.averageRating)
                    ? 'text-yellow-400 fill-yellow-400'
                    : 'text-gray-300'
                }`}
              />
            ))}
          </div>
          <p className="text-sm text-muted-foreground">
            {court.courtType} Court | {court.surfaceType} Surface
          </p>
        </div>
      </Card>
    </Link>
  );
}
